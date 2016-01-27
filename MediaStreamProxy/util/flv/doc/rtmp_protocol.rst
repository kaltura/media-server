RTMP protocol documentation
===========================

:Author:  Marti Raudsepp <marti AT juffo org>
:Date:    2007-11-26
:License: All text is available under the terms of the GNU Free Documentation
          License.

.. contents:: Table of contents


1. About this document
----------------------

This document aims to be a conceptual description of the Flash RTMP protocol,
also known as Real-Time Messaging Protocol.

When I first started writing my implementation of RTMP in Python (just for fun,
to see what information my Flash player is actually leaking for me), I found
plenty of documents that explained low-level details of the protocol, but they
were all spotty and seemed like reverse engineering-time "brain dumps" rather
than documents giving a coherent picture of the protocol's workings.

This document aims to be "the" introduction to the RTMP protocol, starting with
the higher-level conceptual picture, and may also cover the lower-level data
structures if I can find the time. However, I can give no guarantees that my
interpretation of the protocol is the "accurate" or the "best" one; if you
disagree with me or have any corrections, I am open to suggestions and
discussion, and of course reports of bugs/omissions.

This document was written to accompany my implementation of RTMP in Python
(currently lacking a name). All of this was derived from publicly available
second-hand information, packet captures and personal interpretation.


2. The big picture
------------------

Note: other implementations of RTMP use different abstractions.

When a RTMP connection is initialized, it first performs a handshake (explained
later). After the handshake phase is finished, it switches to the normal
"messaging" mode.

My interpretation of the RTMP protocol divides the protocol into three layers.
Ordered from lower to higher level, these are:

**The RTMP fragment layer**
  This layer takes care of mutiplexing separate RTMP streams over a single
  connection, and also interleaving fragments from concurrent streams.
  Fragments are typically up to 128 bytes long, and have an overhead between 1
  and 16 bytes (most commonly 1).

**The RTMP stream layer**
  This layer carries a stream of complete messages. There can be up to 62
  concurrent streams in a single connection. Streams are identified by an
  ad-hoc stream ID which is only used for identifying the stream a fragment
  belongs to. Stream IDs can be re-used for different purposes, although
  Adobe's implementation usually does not.

**The message layer**
  This layer consists of data packets/messages carried over a RTMP stream, but
  has no knowledge of the lower "stream" layer. This layer carries shared
  object (SO) events, RPC calls, FLV video, audio, etc.

While the 1st and 2nd layers are tightly coupled to each other and form the
RTMP protocol. The third layer is mostly separate and should be handled
individually. This document mostly deals with RTMP and only covers the
essentials of the data layer.


3. Handshake phase
------------------

Before the server and client can start exchanging packets, the protocol first
goes through a "handshake" phase. This phase does not deal with RTMP format
yet, but unknown blobs of opaque binary data.

client -> server:
    one-byte identifier 0x03, followed by 1536 bytes of opaque data
server -> client:
    one-byte identifier 0x03, followed by 1536 bytes of opaque data.
server -> client:
    copy of the client's 1536-byte blob.
client -> server:
    copy of the server's 1536-byte blob.

The contents of these 1536-byte chunks are unknown, and appear to have no
significance for the protocol or applications. They may simply be filled with
zeroes.

After this the handshake phase is finished, and the client starts by sending a
RTMP connect packet. In other words, the first RTMP packet always starts at an
offset of ``1+(2*1536) = 3073 bytes``.


4. RTMP basics
--------------

All values in the RTMP protocol are in big-endian, or network byte order. That
is, the most significant byte always comes first. The hex string "1a 2b 3c 4d"
refers to the number ``0x1a2b3c4d`` or 439041101 in decimal.

4.1 Fragment header
```````````````````
As explained earlier, messages transmitted over the RTMP protocol are split
into "fragments", which forms the lowest layer of the protocol.

However, this layer is highly dependant on the stream layer and cannot be
parsed correctly without also handling all details at the stream layer. This
means that the protocol is breaks completely with even slight implementation
errors; a single error will desynchronize the parser and it is essentially
impossible to re-sync it because stream states change with every message.

Every fragment starts with a one-byte "init" value. The upper two bits of this
specify the fragment's header length; the lower 6 bits specify the stream ID.
Stream IDs start from number 2 and go up to 63 which makes 62 concurrent
streams total. Fragments from concurrent streams are usually interleaved.

In most programming languages, the header type can be extracted with
``(init >> 6)`` where `init` is the byte's numeric/ordinal value. The stream ID
can be extracted with ``(init & 0x3f)``.

The two header-type bits specify four different header lenghts (including the
byte itself). Each longer field specifies additional header fields, but
includes all the previous ones.

==== ====== =====
type length fields
==== ====== =====
 0     1    1-byte init
 1     4    1b init, 3b magic
 2     8    1b init, 3b magic, 3b msg length, 1b msg type
 3     12   1b init, 3b magic, 3b msg length, 1b msg type, 4b destination handle
==== ====== =====

When a stream ID has not yet been referenced, a new stream is allocated. The
first fragment in a stream will always contain at least an 8-byte header which
specifies the stream's type and initial message length. It is an error not to
specify them.

FIXME: does the peer_id/destination have to be specified too?

All header values except the init byte must be passed down to the stream layer.


4.2 Fragments in stream context
```````````````````````````````
When a stream is first created, the type and size of its first message are
always specified in its first fragment header. It should be noted that the
"type" field does not affect the processing of the stream.

After the fragment layer has parsed a message header, it makes a reference to
the stream's "magic" parameter. If this parameter is ``0xffffff`` (all 24 bits
set), it means that the header of this fragment is followed by a four-byte
"out-of-band" (OOB) data chunk (regardless of the preceding header length). It
is unknown what this data is used for. Note that they are very rare in
practice; in the author's ~900MB collection of packet captures, it only appears
in 91 messages.

.. important::
  Some third-party RTMP implementations incorrectly handle out-of-band data, or
  do not handle it at all. Just to be clear: the OOB header appears for all
  individual messages in a stream until the three-byte magic value is
  overridden in a header. The OOB data does not count towards the actual
  message length.

This is followed by the actual fragment body. Before this can be received from
the connection, the fragment layer checks how many bytes if the stream's
message have been transmitted. The length of the fragment body is the minimum
of bytes remaining in the message, and the connection's chunk size. The chunk
size is 128 bytes by default, unless explicitly changed with the
RTMP_CHUNK_SIZE message.

For example, when the message body size is 260 bytes, the message will be
transmitted in three fragments: 128 bytes, 128 bytes, and then the final 4
bytes. There is no padding, so the next packet starts after the 4th byte.

After the fragment layer has passed the body to the stream layer, it will
continue reading the next message.

Intermediary message fragments (e.g., those which are not aligned on message
boundaries) must not override the stream's message length or type. This means
that the header for intermediary fragments can be at most 4 bytes (not counting
OOB data).

However, not every message must specify its type, length or destination. When
any of these is left unspecified, they are "inherited" from the previous
message in the same stream.


4.3 Example session
```````````````````
TODO: annotated hexdump example


5. Message layer
----------------

TODO!

5.1 Message types
`````````````````

TODO!

