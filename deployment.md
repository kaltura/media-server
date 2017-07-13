## Machine prerequisites:
 - RHEL/CentOS 6.4 or above or Ubuntu 16.0.4 or above
 - WowzaStreamingEngine 4.5.0+
 - Java jre 1.7.
 - kaltura group (gid = 613) or any other group that apache user is associated with.
 - Write access to @WEB_DIR@/content/recorded directory.
 - Read access to symbolic link of @WEB_DIR@/content under @WEB_DIR@/content/recorded:
  ln –s @WEB_DIR@/content @WEB_DIR@/content/recorded/content

## Admin Console:
- Add admin.ini new permissions, see admin.template.ini:
 - FEATURE_LIVE_STREAM_RECORD
 - FEATURE_KALTURA_LIVE_STREAM
 - FEATURE_KALTURA_LIVE_STREAM_TRANSCODE

## media-server Installation:
- Download the install zip from the tag: 
https://github.com/kaltura/media-server/releases/download/rel-4.5.14.78/KalturaWowzaServer-install-4.5.14.78.zip 
- Copy lib folder in the zip into @WOWZA_DIR@/lib/
- Copy ./installation/configTemplates/* from zip to  @WOWZA_DIR@/conf directory.
- Replace all @WOWZA_DIR@/conf/Server.xml.template parameters and rename to Server.xml:
	KALTURA_SERVICE_URL -
	KALTURA_PARTNER_ID
	KALTURA_PARTNER_ADMIN_SECRET
	
- Replace all @WOWZA_DIR@/conf/kLive/Application.xml.template parameters and rename to Application.xml:
	KALTURA_SERVICE_URL -
