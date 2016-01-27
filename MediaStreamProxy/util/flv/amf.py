# amf.py, a library for parsing AMF messages and RTMP shared object packets
# Copyright (C) 2007-2009  Marti Raudsepp <marti@juffo.org>
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

"""Library for parsing AMF messages and RTMP shared object packets.

This library can parse pure AMF (ActionScript Message Format) messages and also
SO (shared object) events sent over RTMP.

Nearly all functions in this module operate on a file-like stream object. In
the general case, you'd be using cStringIO.StringIO for that.

To parse AMF messages from an RTMP connection, use:
for RTMP_INVOKE,     amf_parse_all(StringIO(msg.data))
for RTMP_SHARED_OBJ, so_parse_packet(StringIO(msg.data))
for RTMP_FLEX_SO,    flex_so_parse_packet(StringIO(msg.data))
for RTMP_FLEX_MSG,   flex_msg_parse_packet(StringIO(msg.data))
"""

import struct
from cStringIO import StringIO

from util import enum, readstruct

class amf_type(enum, int):
  """Enumeration of AMF datatypes"""
  AMF_NUMBER    = 0x0
  AMF_BOOLEAN   = 0x1
  AMF_STRING    = 0x2
  AMF_OBJECT    = 0x3
  AMF_MOVIECLIP = 0x4
  AMF_NULL      = 0x5
  AMF_UNDEF     = 0x6
  AMF_REF       = 0x7
  AMF_MIXARRAY  = 0x8
  AMF_TERM      = 0x9  # terminator for objects and arrays
  AMF_ARRAY     = 0xa
  AMF_DATE      = 0xb
  AMF_LONGSTR   = 0xc
  AMF_VACANT    = 0xd
  AMF_RECORDSET = 0xe
  AMF_XML       = 0xf
  AMF_TYPOBJECT = 0x10
  AMF_AMF3      = 0x11

amf_type.define_enum(globals())

class soevent_type(enum, int):
  """Enumeration of shared object (SO) event types"""
  SO_SERVER_CONNECT    = 0x1
  SO_SERVER_DISCONNECT = 0x2
  SO_SERVER_SETATTR    = 0x3
  SO_CLIENT_UPDDATA    = 0x4
  SO_CLIENT_UPDATTR    = 0x5
  #SO_SERVER_SENDMSG    = 0x6    # and SO_CLIENT_SENDMSG
  SO_SENDMSG           = 0x6
  SO_CLIENT_STATUS     = 0x7
  SO_CLIENT_CLEARDATA  = 0x8
  SO_CLIENT_DELDATA    = 0x9
  SO_SERVER_DELATTR    = 0xa
  SO_CLIENT_INITDATA   = 0xb

soevent_type.define_enum(globals())

class AMFError(Exception):
  """Errors from AMF or SO parsing"""
  pass

def at_eof(stream):
  pos = stream.tell()
  stream.seek(0, 2)
  len = stream.tell()
  if pos == len:
    return True
  else:
    stream.seek(pos)
    return False

def amf_read_type(stream):
  byte = stream.read(1)
  if byte == '':
    raise EOFError
  return amf_type(ord(byte))

#def eof(stream):
#  return stream.tell() >= stream.len

def amf_parse_object(stream):
  obj = {}
  while True:
    key = amf_parse_string(stream)
    if key == '':
      term = amf_read_type(stream)
      if term != AMF_TERM:
        raise AMFError("illegal object terminator: %02x != 09" % term)
      break
    value = amf_parse_any(stream)
    obj[key] = value

  return obj

def amf_parse_all(stream):
  lst = []
  while not at_eof(stream):
    lst.append(amf_parse_any(stream))
  return lst

def amf_parse_string(stream, fmt='>H'):
  (size,) = readstruct(stream, fmt)
  #print 'amf_parse_string @ %08x size=%d' % (stream.tell(), size)
  rawstr = stream.read(size)
  if len(rawstr) < size:
    raise EOFError

  return rawstr
  #try:
  #  return rawstr.decode('utf8')
  #except UnicodeDecodeError, err:
  #  raise AMFError(str(err))

def amf_parse_any(stream):
  type = amf_read_type(stream)
  #print 'amf_parse_any @ %08x type=%r' % (stream.tell()-1, type)
  return amf_parse_specific(stream, type)

def amf_parse_array(stream):
  array = []
  length = readstruct(stream, '>I')[0]

  for i in xrange(length):
    array.append(amf_parse_any(stream))

  return array

def amf_parse_specific(stream, type):
  #type = amf_type(ord(typecode))
  if type == AMF_STRING:
    value = amf_parse_string(stream)
  elif type == AMF_BOOLEAN:
    value = bool(readstruct(stream, '>B')[0])
  elif type == AMF_NUMBER:
    value = readstruct(stream, '>d')[0]
  elif type == AMF_NULL:
    value = None
  elif type in [AMF_UNDEF, AMF_VACANT]:
    value = type
  elif type == AMF_OBJECT:
    value = amf_parse_object(stream)
  elif type == AMF_XML or type == AMF_LONGSTR:
    # just a string with a 32-bit integer size header
    value = amf_parse_string(stream, '>i')
  elif type == AMF_DATE:
    value = readstruct(stream, '>dh')
  elif type == AMF_MIXARRAY:
    value = (readstruct(stream, '>i'), amf_parse_object(stream))
  elif type == AMF_ARRAY:
    value = amf_parse_array(stream)
  else:
    raise AMFError("AMF type %r[%02x] not implemented" % (type, type))
  return value

def so_parse_event(stream):
  """Parse a single SO event.

  Returns tuple (type, key, value). Event type interpretations taken from red5
  RTMPProtocolDecoder.doDecodeSharedObject -- blame them if it doesn't work ;)
  """
  type, size = readstruct(stream, '>B i')
  type = soevent_type(type)

  chunk = stream.read(size)
  if len(chunk) != size:
    raise EOFError

  #print "event %r[%02x] size=%d chunk=%r" % (type, type, size, chunk)

  chunk = StringIO(chunk)

  if type == SO_CLIENT_STATUS:
    key = amf_parse_string(chunk)
    value = amf_parse_string(chunk)

  elif type == SO_CLIENT_UPDDATA:
    key = None
    value = {}
    while not at_eof(chunk):
      tmp_key = amf_parse_string(chunk)
      value[tmp_key] = amf_parse_any(chunk)

  elif type == SO_SENDMSG:
    key = amf_parse_any(chunk)
    value = amf_parse_all(chunk)

  elif size > 0:
    key = amf_parse_string(chunk)
    if at_eof(chunk):
      value = None
    else:
      value = amf_parse_any(chunk)

  else:
    key = value = None

  return type, key, value

def so_parse_packet(stream):
  """Parse SO packet, consisting of multiple SO events"""
  name = amf_parse_string(stream)
  # RTMPProtocolDecoder.decodeSharedObject
  version, persistent, unknown = readstruct(stream, '>i i i')

  events = []
  while not at_eof(stream):
    events.append(so_parse_event(stream))
  return name, version, persistent, unknown, events

def flex_so_parse_packet(stream):
  """Parse a Flex Shared Object packet"""
  encoding = readstruct(stream, '>B')[0]
  if encoding != 0:
    raise AMFError("Packets coded in AMFv%d not supported" % encoding)

  name = amf_parse_string(stream)

  version, persistent, unknown = readstruct(stream, '>i i i')

  events = []
  while not at_eof(stream):
    events.append(so_parse_event(stream))
  return name, version, persistent, unknown, events

def flex_msg_parse_packet(stream):
  """Parse a Flex Message packet"""
  encoding = readstruct(stream, '>B')[0]
  if encoding != 0:
    raise AMFError("Packets coded in AMFv%d not supported" % encoding)

  return amf_parse_all(stream)

