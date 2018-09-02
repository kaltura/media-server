#!/bin/bash
mkdir -p /usr/local/WowzaStreamingEngine/applications/kLive

cd /usr/local/WowzaStreamingEngine/conf

if [[ -n "$MY_POD_NAME" ]]; then
    EC2_REGION="`echo $MY_NODE_NAME | cut -d'.' -f2`"
    SERVER_NODE_TAG="${MY_POD_NAME}.${EC2_REGION}"
    echo "setting SERVER_NODE_TAG $SERVER_NODE_TAG"
fi

if [ -z "$DISABLE_SERVER_NODE_CONF_UPDATE" ]; then
        source /sbin/updateServerNodeConfiguration.sh
fi

# replace config
sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" \
    -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" \
    -e "s#@KALTURA_PARTNER_ADMIN_SECRET@#$PARTNER_ADMIN_SECRET#g"\
    -e "s#@HOST_NAME@#$SERVER_NODE_HOST_NAME#g"\
     Server.xml.template > Server.xml

sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" \
    -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" \
    -e "s#streamName#partnerId/$PARTNER_ID/streamName#g" \
    ./kLive/Application.xml.template > ./kLive/Application.xml

# set log folder
mv log4j.properties log4j.properties.template
sed -e "s#/var/log/#$WOWZA_LOG_DIR/#g" log4j.properties.template > log4j.properties

exec /sbin/entrypoint.sh
