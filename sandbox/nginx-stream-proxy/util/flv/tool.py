#!/usr/bin/env python
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

import sys
import rtmp
import amf
from util import pretty_hex, safe_ascii
from cStringIO import StringIO

help_string = """
options:
    --debug-rtmp     output RTMP fragment debugging/decode information
    --dump-amf       output recognized AMF packets (implies --parse-amf)
    --dump-msg[=FMT] dump complete decoded messages. FMT may be:
        pretty  pretty delimited hex, like 'hexdump -C' (default)
        ascii   ASCII printable characters only (everything else is '.')
        hex     raw hex output, not delimited
        none    do not dump message bodies.
    --parse-amf      parse recognized AMF packets
    --seek=N         seek to the specified offset in the input file. defaults
                     to skipping length of the handshake (3073 bytes)
    --traceback      print traceback and quit on errors
"""

#### frontend

def arg_val(s):
  """Extract trailing string after '='"""
  return s[s.index('=') + 1:]


class RTMPTool(object):
  def __init__(self):
    # default configuration
    self.dump_msg = False
    self.traceback = False
    self.parse_amf = False
    self.dump_amf = False
    self.seek_offset = rtmp.HANDSHAKE_LEN

    self.rtmp = rtmp.RTMPParser()

  def set_file(self, filename):
    self.filename = filename
    if filename == '-':
      self.stream = sys.stdin
      self.stream.read(self.seek_offset)
    else:
      self.stream = open(filename, 'rb')
      self.stream.seek(self.seek_offset)

  def dump_rtmp(self):
    self.rtmp.set_stream(self.stream)
    while True:
      msg = self.rtmp.get_msg()
      if not msg:
        break

      #if self.dump_msg and msg.type not in [rtmp.RTMP_VIDEO_DATA, rtmp.RTMP_AUDIO_DATA]:
      if self.dump_msg:
        self.rtmp_message(msg)
      if self.parse_amf and msg.type in [rtmp.RTMP_INVOKE,
          rtmp.RTMP_SHARED_OBJ, rtmp.RTMP_FLEX_SO, rtmp.RTMP_FLEX_MSG]:

        self.amf_message(msg)
    return 0

  def rtmp_file(self, filename):
    try:
      self.set_file(filename)
      self.dump_rtmp()
      return 0
    except (rtmp.RTMPError, amf.AMFError, IOError, EOFError, AssertionError), err:
      print>>sys.stderr, "%s: %s: %s: %s" % (self.appname, filename, err.__class__, err)
      if self.traceback: raise
      return 1

  def rtmp_message(self, msg):
    # print header
    print msg
    
    if self.dump_msg == 'none':
      return
    elif self.dump_msg == 'hex':
      print msg.data.encode('hex')
    elif self.dump_msg == 'ascii':
      print safe_ascii(msg.data)
    elif self.dump_msg == 'pretty':
      pretty_hex(msg.data)

  def amf_message(self, msg):
    stream = StringIO(msg.data)
    try:
      if msg.type == rtmp.RTMP_INVOKE:
        data = amf.amf_parse_all(stream)
      elif msg.type == rtmp.RTMP_SHARED_OBJ:
        data = amf.so_parse_packet(stream)
      elif msg.type == rtmp.RTMP_FLEX_SO:
        data = amf.flex_so_parse_packet(stream)
      elif msg.type == rtmp.RTMP_FLEX_MSG:
        data = amf.flex_msg_parse_packet(stream)
    except (amf.AMFError, EOFError, AssertionError), err:
      print>>sys.stderr, 'error in message: %r (%r)' % (msg, msg.data)
      print>>sys.stderr, '%s: %s: %s: %s' % (self.appname, self.filename, err.__class__, err)
      if self.traceback: raise
    else:
      if self.dump_amf:
        print "[%02d] %r %r" % (msg.index, msg.type, data)

  def handle_option(self, arg):
    if arg == '--debug-rtmp':
      self.rtmp.debug += 1
    elif arg.startswith('--dump-msg='):
      val = arg_val(arg)
      if val not in ['ascii', 'pretty', 'hex', 'none']:
        print>>sys.stderr, "--dump-msg value must be one of: ascii, pretty, hex, none"
        sys.exit(1)
      self.dump_msg = val
    elif arg == '--dump-msg':
      self.dump_msg = 'pretty'
    elif arg == '--traceback':
      self.traceback = True
    elif arg == '--parse-amf':
      self.parse_amf = True
    elif arg == '--dump-amf':
      self.parse_amf = True
      self.dump_amf = True
    elif arg.startswith('--seek='):
      try:
        self.seek_offset = int(arg_val(arg))
      except ValueError, err:
        print str(err)
        sys.exit(1)
    else:
      print>>sys.stderr, "Unrecognized option: '%s'!" % (arg,)
      self.usage()
      sys.exit(1)

  def usage(self):
    print "usage: %s [options] INFILE [INFILE...]" % (self.appname)
    print help_string

  def run_args(self, args, appname='rtmptool'):
    ret = 0
    self.appname = appname

    if len(args) < 1:
      self.usage()
      return 0

    for arg in sys.argv[1:]:
      if arg.startswith('-') and len(arg) > 1:
        self.handle_option(arg)
      else:
        self.rtmp_file(arg)

    return ret

if __name__ == '__main__':
  tool = RTMPTool()
  ret = tool.run_args(sys.argv[1:], sys.argv[0])
  sys.exit(ret)

