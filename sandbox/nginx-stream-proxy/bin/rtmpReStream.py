#!/usr/bin/env python


"""re-stream RTMP 
"""

import sys
from time import sleep
import socket
import thread

class RTMPError(Exception):
  """Exception raised on RTMP protocol errors
  
  These errors normally indicate complete failure; it is not feasible to
  re-synchronize with the stream."""
  pass

def __read_from_socket__(app):
	app.echo("__read_from_socket__",)
	while True:
		responce = app.socket.recv(1024)
		if not responce:
			break;	
		elif app.responces:
			app.responces.write(responce);
		#else:
			#print responce[10:]

class App(object):
  def __init__(self):
 	self.files = []
	self.socket = self.index = self.rtmp_file = self.responces = None
  def echo (self,fmt, *varargs):
	if self.socket:
		print fmt.format(*varargs)

  def __readData__(self,bytes2read):
    if not self.rtmp_file and not self.__open_next_file__():
	return 
    data = self.rtmp_file.read(bytes2read)
    if not data and self.__open_next_file__():
	 data = self.rtmp_file.read(bytes2read)
    return data;	

  def __parseLine__(self,line):
	t = line.split(" ")
	if not t or len(t) != 2:
	  return 
	t[0]=int(t[0])
	t[1]=int(t[1]) 	
	return t

  def __restream__(self):
	lastTimestamp = -1
	leftOver = 0
	t = []	
	while True:
		if leftOver <= 0:
    		  line = self.index.readline(1024)
		  if not line:
		   self.echo("failed to read line from index file (must be an EOF)")
		   break;		
		  t = self.__parseLine__(line)
		  #print 't[0]={0} t[0]={1} len={2}'.format(t[0],t[1],len(t))
		  if not t:
		   self.echo("failed to parse index line <{0}> (could be close to EOF)",line)
		   break;
		else:
		   self.echo("leftOver = ",leftOver)
		data = self.__readData__(t[1])
		if not data:
		   self.echo("failed to read {0} bytes",t[1])
		   break;
		leftOver = t[1] - len(data);
		if lastTimestamp > 0:
		   msDiff = t[0] - lastTimestamp
		   if msDiff > 0:	
		      #print 'sleeping {0} msec',msDiff)	
		      sleep(msDiff / 1000.0)
		lastTimestamp = t[0]	
		if self.socket:
		  #print 'sending {0}',len(data))
		  self.socket.send(data)
		else:
		  sys.stdout.write(data)		
		
  def __open_next_file__(self):
	if self.rtmp_file:
	   self.rtmp_file.close()
	if not len(self.files):
		self.echo("no more input files")
		return 	
	fileName = self.files.pop()
	self.echo("opening rtmp data file {0}", fileName)
	if fileName == "-":
		fileName = sys.stdin
 	else:
	    self.rtmp_file = open(fileName, 'rb');
  	if not self.rtmp_file:
	     raise RTMPError("failed open file {0}".format(filename))
	return self.rtmp_file;
  
 
  def run_args(self, args, appname='rtmpReStream.py'):
    self.appname = appname
    if  not len(args):
	self.usage()
	return 1 
    isresp = issrc = isidx = isdst = False
    for arg in args:
      if arg == '--src':
	  issrc = True
      elif arg == '--dst':
	 isdst = True
	 isresp = issrc = isidx = False
      elif arg == '--idx':
	isidx = True
	isresp = issrc = isdst = False
      elif arg == '--re': 	
	isresp = True
	isidx = issrc = isdst = False
      elif arg.startswith('--'):
        self.usage()
        return 1
      else:
	#print "arg=  src= idx= dst=", arg,issrc,isidx,isdst
	if   issrc:
	  #print "src file ",arg
	  self.files.append(arg)
	elif isdst:
	  if arg != "-":
		  ind = arg.find(":")
		  if ind != -1:
			t=(arg[:ind],int(arg[ind+1:]))
		  else:
			t=(arg,1935)
		  self.socket = socket.socket(socket.AF_INET,socket.SOCK_STREAM,0)
		  self.echo('connecting to host {0} port {1}',t[0],t[1])
		  if -1 == self.socket.connect(t):
			raise RTMPError('failed connect to  rtmp server {0}:{1}'.format(t[0],t[1]))
		  thread.start_new_thread ( __read_from_socket__, (self,) )
	elif isidx:
	   self.index = open(arg, 'r');
 	elif isresp:
	   self.responces = open(arg, 'wb');
  	   if not self.responces:
	     raise RTMPError("failed open rtmp responce file {0}".format(arg) )
  
    if not self.index:
      raise RTMPError("failed open rtmp index file")
    # pop removes last object so revert the list	
    self.files.reverse()
    self.__restream__()

    del self	
 
  def usage(self):
    print "usage: %s [--src] <rtmp_file(s)> [--idx] <path to rtmp index file>  [--dst] <rtmp server ip address[:port]> [--re] <optional, file to save server responces>" % (self.appname)
    print "INFILE may be '-' for stdin"

  def __del__(self):
	if self.socket:
	  self.socket.close()
	if self.rtmp_file:
	  self.rtmp_file.close()
	if self.index:
	  self.index.close()
	if self.responces:
	  self.responces.close()

if __name__ == '__main__':
  app = App()
  ret = app.run_args(sys.argv[1:], sys.argv[0])
  sys.exit(ret)

