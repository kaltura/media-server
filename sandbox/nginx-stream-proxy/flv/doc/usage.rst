Using RTMP Tool
===============

:Author: Marti Raudsepp <marti AT juffo org>
:Date:   2007-12-02

Abstract
--------

RTMP (Real Time Messaging Protocol) is the protocol used by Adobe Flash for
live audio and video streaming and real-time communication.

RTMP Tool is a set of tools to analyze and decode RTMP packet dumps. RTMP Tool
is licensed under the GNU Lesser General Public License version 2.1 (LGPL).

Quickstart
----------

::

  # capture TCP streams on port 1935
  nice -n-5 tcpflow -p -v -i eth0 tcp port 1935
  # dump all decoded AMF packets
  ./tool.py --dump-amf FILENAME > OUTFILE
  # or dump video stream(s) into a FLV file:
  ./rtmp2flv.py --video FILENAME FLV_FILE

Capturing packets
-----------------

First of all, note that RTMP Tool does not work with *packet* dumps: instead,
it works on dumps of TCP streams. To get these dumps, you can use the handy
``tcpflow`` tool by Jeremy Elson. You can still capture packets using your
favourite pcap-based tool, but you'll have to convert them with the ``tcpflow``
program (alternatively you can use Ethereal, but it's rather tedious if you're
using it more than once).

To avoid dropping packets, you should nice the packet capture tool with a
negative value. Due to the fragility of the RTMP protocol, RTMP Tool cannot
recover from packet loss.

The suggested command line to capture these TCP dumps using ``tcpflow`` is
(assuming your network interface is ``eth0``)::

  nice -n-5 tcpflow -p -v -i eth0 tcp port 1935

To break a pcap dump into separate stream dump files, use this::

  tcpflow -r dump.pcap tcp port 1935

Note that ``tcpflow`` will create a bunch of files in your working directory, two per TCP connection -- one in both directions.

