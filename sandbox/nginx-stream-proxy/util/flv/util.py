# Copyright (C) 2007-2014  Marti Raudsepp <marti@juffo.org>
# All rights released.

import struct

class enum:
  """Simple but handy enum class""" 
  def __repr__(self):
    if self in self.reverse:
      return self.reverse[self]
    else:
      return '%s(%s)' % (self.__class__.__name__, int.__repr__(self))
  @classmethod
  def define_enum(cls, scope=None):
    pairs = dict(((sym, cls(val)) for (sym, val) in cls.__dict__.iteritems() if sym[0] != '_'))
    #cls.__dict__.update(pairs)
    cls.reverse = dict((val, sym) for sym, val in pairs.iteritems())
    if scope:
      scope.update(pairs)

def readstruct(file, fmt):
  size = struct.calcsize(fmt)
  buffer = file.read(size)
  if len(buffer) < size:
    raise EOFError, "end of file, read %d/%d bytes" % (len(buffer), size)
  return struct.unpack(fmt, buffer)

safe_mapping = '.'*32 + ''.join(map(chr, range(32, 127))) + '.'*129
def safe_ascii(data):
  """Replaces all non-ASCII characters with a '.'"""
  return data.translate(safe_mapping)

def pretty_hex(data):
  """Formats binary data in hex just like 'hexdump -C'"""
  for pos in xrange(0, len(data), 16):
    buf = data[pos:pos+16]
    hexes = ' '.join(c.encode('hex') for c in buf)
    print '%06x: %-48s |%-16s|' % (pos, hexes, safe_ascii(buf))

def ms2str(time):
  """Converts a time in ms to a human readable string"""
  s = time / 1000
  m, s = divmod(s, 60)
  h, m = divmod(m, 60)
  return "%d:%02d:%02d" % (h, m, s)
