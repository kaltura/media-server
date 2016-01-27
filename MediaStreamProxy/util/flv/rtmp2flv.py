#!/usr/bin/env python

# Copyright (C) 2007-2014  Marti Raudsepp <marti@juffo.org>
# All rights released.

"""Tool to convert RTMP capture files into FLV video files.
"""

import sys
import rtmp
import struct
from util import pretty_hex

def int24(i):
  """Converts an integer into a 3-byte binary representation."""
  return struct.pack('>I', i)[1:4]

FLV_AUDIO = 4
FLV_VIDEO = 1
FLV_TAG_AUDIO = 0x08
FLV_TAG_VIDEO = 0x09
FLV_TAG_META  = 0x12
FLV_TAG_INVOKE = 0x14

class FLVOut(object):
  def __init__(self, stream):
    self.stream = stream

  def writestruct(self, format, *args):
    self.stream.write(struct.pack(format, *args))

  def header(self, type):
    # typecode
    self.stream.write('FLV\x01')
    # flags and offset
    self.writestruct('>BI', type, 9)
    # previous tag size
    self.writestruct('>I', 0)

  def write_tag(self, tagtype, data, time=0):
    self.writestruct('>B 3s 3s B 3s', tagtype, int24(len(data)), int24(time), time>>24, int24(tagtype))
    self.stream.write(data)
    # length of the previous tag
    self.writestruct('>I', len(data))

class App(object):
  def __init__(self):
    self.rtmp = rtmp.RTMPParser()
    self.filter = [rtmp.RTMP_AUDIO_DATA, rtmp.RTMP_VIDEO_DATA, rtmp.RTMP_FLV_DATA, rtmp.RTMP_INVOKE ]
    self.outputs = {}
 	
  def write_packed(self, out, packed, time):
    """Write RTMP type 0x16 messages
    They are prepacked FLV audio & video tags with headers
    If timestamps in packed tags differ from RTMP message they must be rescaled"""

    if len(packed) < 15:
      if self.rtmp.debug:
        pretty_hex(packed)
      raise rtmp.RTMPError("short RTMP_FLV_DATA packet")

    time_low = struct.unpack('>I', '\x00' + packed[4:7])[0]
    time_high = struct.unpack('B', packed[7])[0]
    delta = (time_high << 24 | time_low) - time

    offset = 0
    while len(packed) >= offset + 16:
      type = rtmp.rtmp_type(struct.unpack('B', packed[offset])[0])
      assert type in [rtmp.RTMP_AUDIO_DATA, rtmp.RTMP_VIDEO_DATA], "Unknown RTMP_FLV_DATA content"
      size = struct.unpack('>I', '\x00' + packed[offset+1:offset+4])[0]

      time_low = struct.unpack('>I', '\x00' + packed[offset+4:offset+7])[0]
      time_high = struct.unpack('B', packed[offset+7])[0]
      scaled_time = (time_high << 24 | time_low) - delta
      #print "offset: 0x%x, type: %r, size: %d, time_low: %d, time_high: %d, scaled_time: %d" % (offset, type, size, time_low, time_high, scaled_time)

      assert len(packed) >= offset + size + 15
      out.write_tag(type, packed[offset+11:offset+11+size], scaled_time)
      offset += size + 15

  def createFLV(self,stream_name,index):
     if hasattr(self,'prefix'):	
	i = stream_name.find("?")
	if i != -1:
	   stream_name = stream_name[:i]
	#print "createFLV: ", (self.prefix + '-' + stream_name + '.flv')
        self.outputs[index] = FLVOut(open(self.prefix + stream_name + '.flv', 'wb'))
        self.outputs[index].header(FLV_AUDIO|FLV_VIDEO)
     elif not hasattr(self,'out'): 
	#print "createFLV: %s %d from stdin", (stream_name,index)
	self.out = FLVOut(self.out_file)
    	self.out.header(FLV_AUDIO|FLV_VIDEO)

  def get_output(self,index):
	if hasattr(self,'out'):
	   return self.out
	else:
	   return self.outputs[index];	
	
  def convert(self):
    self.src_file.seek(rtmp.HANDSHAKE_LEN)
    self.rtmp.set_stream(self.src_file)
  
    while True:
      msg = self.rtmp.get_msg()
      if not msg:
        break
  

      if msg.type in self.filter and len(msg.data) > 0:
        if msg.type == rtmp.RTMP_INVOKE:
	  msg.data[3:10]  	
	  if( len(msg.data) > 10 and msg.data[3:10] == 'publish' ):
	     self.createFLV(msg.data[23:23 + int( (ord(msg.data[21]) << 8) or ord(msg.data[22]))],msg.index)
        elif msg.type == rtmp.RTMP_FLV_DATA:
	  self.write_packed(self.get_output(msg.index), msg.data, msg.absolute_time)
        else:
	  self.get_output(msg.index).write_tag(msg.type, msg.data, msg.absolute_time)

  def run_args(self, args, appname='rtmp2flv.py'):
    self.appname = appname

    files = []
    for arg in args:
      if arg == '--audio':
        self.filter = [rtmp.RTMP_AUDIO_DATA]
      elif arg == '--video':
        self.filter = [rtmp.RTMP_VIDEO_DATA]
      elif arg == '--av':
        self.filter = [rtmp.RTMP_AUDIO_DATA, rtmp.RTMP_VIDEO_DATA]
      elif arg.startswith('--'):
        self.usage()
        return 1
      else:
        files.append(arg)

    if len(files) < 1 or len(files) > 2:
      self.usage()
      return 1

    if files[0] != '-':
      self.src_file = open(files[0], 'rb')
    else:
      self.src_file = sys.stdin

    if len(files) >= 2 and files[1] != '-':
     self.prefix = files[1]
    else:
      self.out_file = sys.stdout
     
    self.convert()

  def usage(self):
    print "usage: %s [--audio] [--video] [--av] INFILE [OUTFILE]" % (self.appname)
    print "INFILE and OUTFILE may be '-' for stdin or stdout respectively"


if __name__ == '__main__':
  app = App()
  ret = app.run_args(sys.argv[1:], sys.argv[0])
  sys.exit(ret)

