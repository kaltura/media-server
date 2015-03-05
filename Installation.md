# Kaltura Server #



## Plugins: ##
- Add Wowza to plugins.ini.

## Configuration ##
- Add the IP range containing the Wowza machine IP to the @APP_DIR@/configurations/local.ini:  
	internal_ip_range = {required range}  
  Note that this is not necessary for a Hybrid eCDN installation.
- Edit the @APP_DIR@/configurations/broadcast.ini file according to the broadcast.template.ini file.
- If there is a need for non-default configuration of the WSE (for instance, different port), you will need to create a custom configuration file on your API machine under @APP_DIR@/configurations/media_servers.ini, according to the template found here: https://github.com/kaltura/media-server/blob/3.0.8/media_servers.template.ini.

## Admin Console: ##
- Add admin.ini new permissions, see admin.template.ini:
 - FEATURE_LIVE_STREAM_RECORD
 - FEATURE_KALTURA_LIVE_STREAM
 - FEATURE_KALTURA_LIVE_STREAM_TRANSCODE



## Edge Servers: ##
media_servers.ini is optional and needed only for custom configurations.

- application - defaults to kLive
- search_regex_pattern, replacement - the regular expression to be replaced in the machine name in order to get the external host name.
- domain - overwrites the machine name and the regular expression replacement with a full domain name.
- port - defaults to 1935.
- port-https - no default defined.





# Wowza #



## Prerequisites: ##
- Wowza media server 4.1.0 or above.
- Java jre 1.7.
- kaltura group (gid = 613) or any other group that apache user is associated with.
- Write access to @WEB_DIR@/content/recorded directory.
- Read access to symbolic link of @WEB_DIR@/content under @WEB_DIR@/content/recorded:
  ln –s @WEB_DIR@/content @WEB_DIR@/content/recorded/content


## Additional libraries: ##
- commons-codec-1.4.jar
- commons-httpclient-3.1.jar
- commons-logging-1.1.1.jar
- commons-lang-2.6.jar




## For all wowza machine: ##
- Copy [KalturaWowzaServer.jar](https://github.com/kaltura/media-server/releases/download/rel-3.0.8.1/KalturaWowzaServer-3.0.8.1.jar "KalturaWowzaServer.jar") to @WOWZA_DIR@/lib/
- Copy additional jar files (available in Kaltura Java client library) to @WOWZA_DIR@/lib/
 - [commons-codec-1.4.jar](https://github.com/kaltura/server-bin-linux-64bit/raw/master/wowza/commons-codec-1.4.jar "commons-codec-1.4.jar")
 - [commons-httpclient-3.1.jar](https://github.com/kaltura/server-bin-linux-64bit/raw/master/wowza/commons-httpclient-3.1.jar "commons-httpclient-3.1.jar")
 - [commons-logging-1.1.1.jar](https://github.com/kaltura/server-bin-linux-64bit/raw/master/wowza/commons-logging-1.1.1.jar "commons-logging-1.1.1.jar") 
 - [commons-lang-2.6.jar](https://github.com/kaltura/server-bin-linux-64bit/raw/master/wowza/commons-lang-2.6.jar "commons-lang-2.6.jar")
- Delete all directories under @WOWZA_DIR@/applications, but not the applications directory itself.
- Create @WOWZA_DIR@/applications/kLive directory.
- Delete all directories under @WOWZA_DIR@/conf, but not the conf directory itself.
- Create @WOWZA_DIR@/conf/kLive directory.
- Copy @WOWZA_DIR@/conf/Application.xml to @WOWZA_DIR@/conf/kLive/Application.xml

**Edit @WOWZA_DIR@/conf/kLive/Application.xml:**

 - /Root/Application/Name - kLive
 - /Root/Application/AppType - Live
 - /Root/Application/Streams/StreamType - live
 - /Root/Application/Streams/StorageDir - @WEB_DIR@/content/recorded
 - /Root/Application/Streams/LiveStreamPacketizers: 
	 - cupertinostreamingpacketizer
	 - mpegdashstreamingpacketizer
	 - sanjosestreamingpacketizer
	 - smoothstreamingpacketizer
	 - dvrstreamingpacketizer

 - /Root/Application/Transcoder/LiveStreamTranscoder - transcoder
 - /Root/Application/Transcoder/Templates - `http://@WWW_HOST@/api_v3/index.php/service/wowza_liveConversionProfile/action/serve/streamName/${SourceStreamName}/f/transcode.xml`
 - /Root/Application/Transcoder/Properties:
```xml
<Property>
	<Name>sortPackets</Name>
	<Value>true</Value>
	<Type>Boolean</Type>
</Property>
<Property>
	<Name>sortBufferSize</Name>
	<Value>4000</Value>
	<Type>Integer</Type>
</Property>
```

 - /Root/Application/DVR/Recorders - dvrrecorder
 - /Root/Application/DVR/Store - dvrfilestorage
 - /Root/Application/DVR/Properties:
```xml
<Property>
	<Name>httpRandomizeMediaName</Name>
	<Value>true</Value>
	<Type>Boolean</Type>
</Property>
<Property>
	<Name>dvrAudioOnlyChunkTargetDuration</Name>
	<Value>10000</Value>
	<Type>Integer</Type>
</Property>
<Property>
	<Name>dvrChunkDurationMinimum</Name>
	<Value>10000</Value>
	<Type>Integer</Type>
</Property>
<Property>
	<Name>dvrMinimumAvailableChunks</Name>
	<Value>5</Value>
	<Type>Integer</Type>
</Property>
```

 - /Root/Application/HTTPStreamers:
	 - cupertinostreaming
	 - smoothstreaming
	 - sanjosestreaming
	 - mpegdashstreaming
	 - dvrchunkstreaming
 - /Root/Application/LiveStreamPacketizer/Properties:
```xml
<Property>
	<Name>httpRandomizeMediaName</Name>
	<Value>true</Value>
	<Type>Boolean</Type>
</Property>
<Property>
	<Name>cupertinoPlaylistChunkCount</Name>
	<Value>10</Value>
	<Type>Integer</Type>
</Property>
<Property>
	<Name>sanjoseChunkDurationTarget</Name>
	<Value>10000</Value>
	<Type>Integer</Type>
</Property>
<Property>
	<Name>sanjoseMaxChunkCount</Name>
	<Value>10</Value>
	<Type>Integer</Type>
</Property>
<Property>
	<Name>sanjosePlaylistChunkCount</Name>
	<Value>4</Value>
	<Type>Integer</Type>
</Property>
```

 - /Root/Application/HTTPStreamer/Properties:
```xml
<Property>
	<Name>httpOriginMode</Name>
	<Value>on</Value>
</Property>
<Property>
	<Name>cupertinoCacheControlPlaylist</Name>
	<Value>max-age=5</Value>
</Property>
<Property>
	<Name>cupertinoCacheControlMediaChunk</Name>
	<Value>max-age=86400</Value>
</Property>
<Property>
	<Name>cupertinoOnChunkStartResetCounter</Name>
	<Value>true</Value>
	<Type>Boolean</Type>
</Property>
<Property>
	<Name>smoothCacheControlPlaylist</Name>
	<Value>max-age=3</Value>
</Property>
<Property>
	<Name>smoothCacheControlMediaChunk</Name>
	<Value>max-age=86400</Value>
</Property>
<Property>
	<Name>smoothCacheControlDataChunk</Name>
	<Value>max-age=86400</Value>
</Property>
<Property>
	<Name>sanjoseCacheControlPlaylist</Name>
	<Value>max-age=3</Value>
</Property>
<Property>
	<Name>sanjoseCacheControlMediaChunk</Name>
	<Value>max-age=86400</Value>
</Property>
```

 - /Root/Application/Modules, add:
```xml
<Module>
	<Name>LiveStreamEntry</Name>
	<Description>LiveStreamEntry</Description>
	<Class>com.kaltura.media.server.wowza.listeners.LiveStreamEntry</Class>
</Module>
```
 
 - /Root/Application/Properties, add new Property:
```xml
<Property>
	<Name>streamTimeout</Name>
	<Value>200</Value>
	<Type>Integer</Type>
</Property>
<Property>
	<Name>securityPublishRequirePassword</Name>
	<Value>false</Value>
	<Type>Boolean</Type>
</Property>
```



**Edit @WOWZA_DIR@/conf/Server.xml:**

 - /Root/Server/ServerListeners:
```xml
<ServerListener>
	<BaseClass>com.kaltura.media.server.wowza.listeners.ServerListener</BaseClass>
</ServerListener>
```

 - /Root/Server/Properties:
```xml
<Property>
	<Name>KalturaServerURL</Name>
	<Value>http://@WWW_DIR@</Value>
</Property>
<Property>
	<!-- Kaltura media server partner (-5) admin secret -->
	<Name>KalturaServerAdminSecret</Name>
	<Value>@MEDIA_PARTNER_ADMIN_SECRET@</Value>
</Property>
<Property>
	<!-- Kaltura API http timeout -->
	<Name>KalturaServerTimeout</Name>
	<Value>30</Value>
</Property>
<Property>
	<!-- Kaltura server managers to be loaded -->
	<Name>KalturaServerManagers</Name>
	<Value>com.kaltura.media.server.wowza.StatusManager, com.kaltura.media.server.wowza.LiveStreamManager</Value>
</Property>
<Property>
	<!-- Kaltura web services to be loaded -->
	<Name>KalturaServerWebServices</Name>
	<Value>com.kaltura.media.server.api.services.KalturaLiveService</Value>
</Property>
<Property>
	<!-- Kaltura server status reporting interval, in seconds -->
	<Name>KalturaServerStatusInterval</Name>
	<Value>300</Value>
</Property>
<Property>
	<!-- Kaltura interval to update that live stream entry is still broadcasting, in seconds -->
	<Name>KalturaLiveStreamKeepAliveInterval</Name>
	<Value>60</Value>
</Property>
<Property>
	<!-- Kaltura maximum DVR window, in seconds, should be 24 hours -->
	<Name>KalturaLiveStreamMaxDvrWindow</Name>
	<Value>7200</Value>
</Property>
<Property>
	<!-- Kaltura maximum recorded chunk duration, in minutes, should be an hour -->
	<Name>KalturaRecordedChunckMaxDuration</Name>
	<Value>60</Value>
</Property>
<Property>
	<!-- Kaltura web services http port -->
	<Name>KalturaServerWebServicesPort</Name>
	<Value>888</Value>
</Property>
<Property>
	<!-- Kaltura web services binding host name -->
	<Name>KalturaServerWebServicesHost</Name>
	<Value>0.0.0.0</Value>
</Property>
<Property>
	<!-- Kaltura recorded file group -->
	<Name>KalturaRecordedFileGroup</Name>
	<!-- kaltura (gid = 613) or any other group that apache user is associated with. -->
	<Value>kaltura</Value>
</Property>
<Property>
	<!-- Minimum buffering time before registering entry as is-live (in seconds) -->
	<Name>KalturaIsLiveRegistrationMinBufferTime</Name>
	<Value>5</Value>
</Property>
```


**Edit @WOWZA_DIR@/conf/log4j.properties:**
 - Set `log4j.rootCategory` = `INFO`
 - Add `log4j.logger.com.kaltura` = `DEBUG`
 - Comment out `log4j.appender.serverAccess.layout` and its sub values `log4j.appender.serverAccess.layout.*` 
 - Add `log4j.appender.serverAccess.layout` = `org.apache.log4j.PatternLayout`
 - Add `log4j.appender.serverAccess.layout.ConversionPattern` = `[%d{yyyy-MM-dd HH:mm:ss}][%t][%C:%M] %p - %m - (%F:%L) %n`
 - Change `log4j.appender.serverAccess.File` = `@LOG_DIR@/kaltura_mediaserver_access.log`
 - Comment out `log4j.appender.serverError.layout` and its sub values `log4j.appender.serverError.layout.*` 
 - Add `log4j.appender.serverError.layout` = `org.apache.log4j.PatternLayout`
 - Add `log4j.appender.serverError.layout.ConversionPattern` = `[%d{yyyy-MM-dd HH:mm:ss}][%t][%C:%M] %p - %m - (%F:%L) %n` 
 - Change `log4j.appender.serverError.File` = `@LOG_DIR@/kaltura_mediaserver_error.log`
 - Change `log4j.appender.serverStats.File` = `@LOG_DIR@/kaltura_mediaserver_stats.log`



**Setting keystore.jks:**

- [Create a self-signed SSL certificate](http://www.wowza.com/forums/content.php?435 "Create a self-signed SSL certificate") or use existing one.
- Copy the certificate file to @WOWZA_DIR@/conf/keystore.jks


**Edit @WOWZA_DIR@/conf/VHost.xml:**

- Uncomment /Root/VHost/HostPortList/HostPort with port 443 for SSL.
- /Root/VHost/HostPortList/HostPort/SSLConfig/KeyStorePassword - set the password for your certificate file.  


## Add Multicast support (for on-prem installations) 

 - Add the following to Server.xml under /Root/Server/Properties:  
 
 ```xml
<Property>
	<Name>MulticastIP</Name>
	<Value>{multicast IP}</Value>
</Property>
<Property>
	<Name>MulticastPortRange</Name>
	<Value>{multicast port range}</Value>
</Property>
<Property>
	<Name>MulticastTag</Name>
	<!-- change the value of this property if a different tag is required-->
	<Value>multicast_silverlight</Value>
</Property>
<Property>
	<Name>silverlightMulticastAuthorizerList</Name>
	<Value>{multicast IP}</Value>
</Property>
<Property>
<Name>multicastStreamTimeout</Name>
<Value>11000</Value>
<Type>Integer</Type>
</Property>
```

- In the same file, edit the value of the KalturaServerManagers property:  
```xml
<Property>
	<Name>KalturaServerManagers</Name>
	<Value>{previous value}, com.kaltura.media.server.wowza.PushPublishManager</Value>
</Property>
```

- Add the following under /Root/Server/ServerListeners:  

```xml
<ServerListener>
<BaseClass>com.wowza.wms.httpstreamer.smoothstreaming.multicast.ServerListenerSilverlightMulticastAuthorizer</BaseClass>
</ServerListener>
```
# On-Prem specific configuration #

**Important: both of the following configurations require that the Wowza Streaming Engine have access to the @WEB_DIR@ directory used by the Kaltura installation (the same location used by the API, the batch workers, etc)**.

## Using the Wowza as the environment FMS ##
- Create a new VOD application, preferably using the Wowza Streaming Engine Manager application:  
http://{Wowza machine IP}:8088
- Give your application some name, for instance kVOD.
- Use your API to define a new RTMP Delivery Profile for partner 0, according to the documentation, with the following specifications:
    - Type - LOCAL PATH RTMP
    - Streamer Type - RTMP
    - url - http://{Wowza IP}/{VOD application name}/{random string}
    - status - ACTIVE
    - isDefault - TRUE VALUE
    
## For webcam recording servers ##

**Create oflaDemo application**

 - Create oflaDemo application in your Wowza server.
  - Create @WOWZA_DIR@/applications/oflaDemo directory
  - Create @WOWZA_DIR@/conf/oflaDemo directory
  - Copy @WOWZA_DIR@/conf/Application.xml to @WOWZA_DIR@/conf/oflaDemo/Application.xml.
 - Configure @WOWZA_DIR@/conf/oflaDemo/Application.xml
 - /Root/Application/Name - oflaDemo
 - /Root/Application/AppType - Live
 - /Root/Streams/StreamType - live-record
 - /Root/Streams/StorageDir - @WEB_DIR@/content/webcam
 - /Root/Transcoder/LiveStreamTranscoder - transcoder
 - /Root/Transcoder/Templates - hdfvr.xml

 - **Note:** if you are interested in using the webcam recording with the KCW, you will have to reset the Wowza to save the recording files as FLV files. In order to do this:  
     - Edit the Server.xml
     - Change the value of the tag /Root/Server/Streams/DefaultStreamPrefix to 'flv'.


**Create transcoding template**

 - Create @WOWZA_DIR@/transcoder/templates/hdfvr.xml template:

```xml
<Root>
	<Transcode>
		<Encodes>
			<!-- Example Encode block for source, not required unless Member of StreamNameGroup. -->
			<Encode>
				<Enable>true</Enable>
				<Name>aac</Name>
				<StreamName>mp4:${SourceStreamName}</StreamName>
				<Video>
					<!-- H.264, PassThru, Disable -->
					<Codec>PassThru</Codec>
					<Bitrate>${SourceVideoBitrate}</Bitrate>
					<Parameters>
					</Parameters>
				</Video>
				<Audio>
					<!-- AAC, PassThru, Disable -->
					<Codec>AAC</Codec>
					<Bitrate>48000</Bitrate>
				</Audio>
				<Properties>
				</Properties>
			</Encode>
		</Encodes>
		<Decode>
		</Decode>
		<StreamNameGroups>
		</StreamNameGroups>
		<Properties>
		</Properties>
	</Transcode>
</Root>
```

**Configure file system**

 - Make sure that @WEB_DIR@/content/webcam group is kaltura or apache
 - Define permissions stickiness on the group:
  - chmod +t @WEB_DIR@/content/webcam
  - chmod g+s @WEB_DIR@/content/webcam

**Configure UI-Conf**
- Edit the ui-conf of the KCW/KRecorder you are using to record from webcam- replace the {HOST_NAME} token in the uiconf with the hostname:port of the Wowza machine.

## Add Ad-Stitching support ##
**Edit @WOWZA_DIR@/conf/kLive/Application.xml:**

- /Root/Application/Properties, add new Property:
```
            <Property>
					<Name>ApplicationManagers</Name>
					<Value>com.kaltura.media.server.wowza.CuePointsManager</Value>
					<Type>String</Type>
			 </Property>
```
**Edit @WOWZA_DIR@/conf/Server.xml:**

- Under /Root/Server/Properties, edit the following property:
```
            <Property>
				<Name>KalturaServerWebServices</Name>
				<Value>com.kaltura.media.server.api.services.KalturaLiveService,com.kaltura.media.server.api.services.KalturaCuePointsService</Value>
			</Property>
```


