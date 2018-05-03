#!/bin/bash
mkdir -p /usr/local/WowzaStreamingEngine/applications/kLive

cd /usr/local/WowzaStreamingEngine/conf

# replace config
sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" \
    -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" \
    -e "s#@KALTURA_PARTNER_ADMIN_SECRET@#$PARTNER_ADMIN_SECRET#g"\
     Server.xml.template > Server.xml

sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" \
    -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" \
    -e "s#streamName#partnerId/$PARTNER_ID/streamName#g" \
    ./kLive/Application.xml.template > ./kLive/Application.xml

# set log folder
mv log4j.properties log4j.properties.template
sed -e "s#/var/log/#$WOWZA_LOG_DIR/#g" log4j.properties.template > log4j.properties


exec /sbin/entrypoint.sh