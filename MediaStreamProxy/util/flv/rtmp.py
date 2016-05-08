# rtmp.py, a compact Python library for debugging/analyzing RTMP streams
# Copyright (C) 2007-2014  Marti Raudsepp <marti@juffo.org>
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

"""A compact Python library for debugging/analyzing RTMP streams"""

import struct
from util import enum, readstruct, ms2str

hdrfmt = ['>3s iI', '>3s i', '>3s']
DEFAULT_CHUNKLEN = 128
HANDSHAKE_LEN = 2*1536 + 1

class rtmp_type(enum, int):
  """Enumeration of RTMP message types"""
  RTMP_NONE          = 0x00
  RTMP_CHUNK_SIZE    = 0x01
  RTMP_UNKNOWN       = 0x02
  RTMP_BYTES_READ    = 0x03
  RTMP_PING          = 0x04
  RTMP_SERVER        = 0x05
  RTMP_CLIENT        = 0x06
  RTMP_UNKNOWN2      = 0x07
  RTMP_AUDIO_DATA    = 0x08
  RTMP_VIDEO_DATA    = 0x09
  RTMP_UNKNOWN3      = 0x0a
  RTMP_FLEX_SO       = 0x10
  RTMP_FLEX_MSG      = 0x11
  RTMP_NOTIFY        = 0x12
  RTMP_SHARED_OBJ    = 0x13
  RTMP_INVOKE        = 0x14
  RTMP_FLV_DATA      = 0x16

rtmp_type.define_enum(globals())

class RTMPError(Exception):
  """Exception raised on RTMP protocol errors
  
  These errors normally indicate complete failure; it is not feasible to
  re-synchronize with the stream."""
  pass

####

class RTMPStream(object):
  """Implements the RTMP message-stream state machine"""
  __slots__ = ['index', 'len', 'remaining', 'type', 'time', 'peer_id', 'data',
      'oobdata', 'absolute_time', 'streamId']

  def __init__(self, index):
    self.index = index

    self.len = 0
    self.remaining = 0
    self.type = None
    self.time = 0
    self.peer_id = -1
    self.data = ''
    self.oobdata = []
    self.absolute_time = 0
    self.streamId = 0

  def __repr__(self):
    return '<RTMPStream %r %r[%02x] %d/%d>' % (
        self.index, self.type, self.type, len(self.data), self.len)

  def handle_header(self, hdr, hdrType):
    """Apply header to the current stream.
    Returns remaining length of the message."""

    if len(hdr) >= 1:
      # Chunk headers of type 0-2 should only be used at start of a new message
      if self.data != '':
        raise RTMPError("premature start of new message chunk")
      self.time = struct.unpack('>i', '\x00' + hdr[0])[0]

    if len(hdr) >= 2:
      self.len = int(hdr[1] >> 8) # length is stored in the upper 24 bits
      self.remaining = self.len
      self.type = rtmp_type(int(hdr[1] & 0xff))
      if self.type > 0x16:
        raise RTMPError("unhandled RTMP type: 0x%02x" % self.type)
      if self.len > 1024*1024:
        # FIXME: this is a sanity check only
        raise RTMPError("abnormally large message: %d bytes" % self.len)
    else:
      if self.len == 0:
        raise RTMPError("stream %d length/type not specified" % self.index)

    # Chunk message headers of type 0 got absolute timestamps, type 1-3 got
    # relative timestamps. Type 3 is also used for continuation of messages
    # broken up in several chunks. If a chunk is a continuation don't add
    # the timestamp
    if len(hdr) >= 3:
      self.peer_id = hdr[2]
      self.absolute_time = self.time
    elif self.data == '':
      self.absolute_time += self.time

    if hdrType == 0:
      self.streamId = hdr[2]
    return self.remaining

  def feed_oobdata(self, buffer):
    # FIXME: Integrate this 32-bit timestamp field into handle_header()
    # Remove "oobdata" field entirely
    self.oobdata.append((len(self.data), buffer))

  def feed_data(self, buffer):
    assert len(buffer) <= self.remaining, "feed_data(): buffer too long"
    self.data += buffer
    self.remaining -= len(buffer)

    if self.remaining == 0:
      msg = RTMPMessage(self.index, self.type, self.time, self.absolute_time, self.peer_id, self.data, self.streamId)
      self.data = ''
      self.oobdata = []
      self.remaining = self.len
      return msg

####

class RTMPMessage(object):
  """Represents a complete RTMP message"""
  __slots__ = ['index', 'type', 'time', 'absolute_time', 'peer_id', 'data', 'streamId']

  def __init__(self, index, type, time, absolute_time, peer_id, data,streamId = -1):
    self.index = index
    self.type = type
    self.time = time
    self.absolute_time = absolute_time
    self.peer_id = peer_id
    self.data = data
    self.streamId = streamId
  def __repr__(self):
    return '<RTMPMessage st=%d type=%r[%02x] peer=%08x time=%d absolute_time=%d len=%d>' % (
        self.index, self.type, self.type, self.peer_id, self.time, self.absolute_time,
        len(self.data))

####

class RTMPParser(object):
  """RTMP protocol parser class
  
  Usage::
    r = rtmp.RTMPParser()
    r.set_stream(open(...))
    while True:
      msg = r.get_msg()
      if not msg:
        break
      # do something with message here
  """
  debug = 0

  def __init__(self):
    self.reset()

  def reset(self):
    self.streams = {}
    self.queue = []
    self.chunklen = DEFAULT_CHUNKLEN

  def set_stream(self, stream):
    self.reset()
    self.stream = stream

  def read(self, bytes):
    """Read specified number of bytes from stream or raise EOFError"""
    ret = self.stream.read(bytes)
    if len(ret) < bytes:
      raise EOFError("Unexpected end of stream: %d of %d characters read"
          % (len(ret), bytes))
    return ret

  def next_fragment(self):
    """Read and parse the next RTMP fragment"""
    if self.debug:
      offset = self.stream.tell()

    init = ord(self.read(1))
    #except EOFError:
    #  self.queue.append(None)
    #  return
    cstreamid = init & 0x3f
    try:
      stream = self.streams[cstreamid]
    except KeyError:
      stream = self.new_stream(cstreamid)

    if init & 0xc0 != 0xc0:
      hdr = readstruct(self.stream, hdrfmt[init>>6])
    else:
      hdr = ()
    stream.handle_header(hdr,init>>6)

    # if the "time" field has the magic value, the packet header is
    # superseded by a 32-bit extended timestamp
    if stream.time == 0xffffff:
      stream.feed_oobdata(self.read(4))

    chunklen = min(self.chunklen, stream.remaining)
    if self.debug:
      # hdr is left unassigned otherwise
      if init & 0xc0 == 0xc0:
        hdr = None
      #if (init>>6) == 0:

      print 'header@%08x: hdr type=%d cstreamid=%d hdr=%r; \t%d/%d bytes [%r] streamid=%x ' % (
          offset, init>>6, cstreamid, hdr, stream.remaining, chunklen, stream.type, stream.streamId)

    msg = stream.feed_data(self.read(chunklen))

    if msg:
      self.inbound_msg(msg)

  def inbound_msg(self, msg):
    """Handles RTMP-layer control messages and queues higher-layer messages"""
    if msg.type == RTMP_CHUNK_SIZE:
      assert msg.peer_id == 0
      assert len(msg.data) == 4
      chunklen = struct.unpack('>I', msg.data)[0]
        if chunklen > 64*1024:
        # FIXME: this is a sanity check only
        raise RTMPError("abnormal chunk size specified: %d" % chunklen)
      self.chunklen = int(chunklen)
    else:
      self.queue.append(msg)

  def new_stream(self, cstreamid):
    """Create a new RTMPStream object and add it to the 'streams' dict"""
    assert cstreamid not in self.streams
    assert cstreamid < 64
    if cstreamid < 2:
      raise RTMPError("Multi-byte inits not supported (streamid=%d)" %
          (cstreamid))

    stream = RTMPStream(cstreamid)
    self.streams[cstreamid] = stream

    return stream

  def get_msg(self):
    """Reads packets until a new complete message arrives.
    Returns new message, or None on EOF"""
    try:
      while len(self.queue) < 1:
        self.next_fragment()
    except EOFError:
      return None
    return self.queue.pop(0)

