#!/bin/bash
mkdir -p /usr/local/WowzaStreamingEngine/applications/kLive

cd /usr/local/WowzaStreamingEngine/conf

if [[ -n "$MY_POD_NAME" ]]; then
    EC2_REGION="`echo $MY_NODE_NAME | cut -d'.' -f2`"
    SERVER_NODE_TAG="${MY_POD_NAME}.${EC2_REGION}"
    echo "setting SERVER_NODE_TAG $SERVER_NODE_TAG"
fi

if [[ $(command -v nvidia-smi >> /dev/null && nvidia-smi -L | grep -i gpu) ]]; then
    export GPU_SUPPORT="true"
    echo "Setting GPU_SUPPORT as $GPU_SUPPORT"
fi

if [ -z "$DISABLE_SERVER_NODE_CONF_UPDATE" ]; then
        source /sbin/updateServerNodeConfiguration.sh
fi

if [ -z "$PLAY_WHITELIST" ]; then
        PLAY_WHITELIST="*"
        echo "Setting PLAY_WHITELIST=*"
fi

# replace config
sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" \
    -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" \
    -e "s#@KALTURA_PARTNER_ADMIN_SECRET@#$PARTNER_ADMIN_SECRET#g"\
    -e "s#@HOST_NAME@#$SERVER_NODE_HOST_NAME#g"\
    -e "s#@ORIGIN_CUSTOM_HEADER_NAME@#$ORIGIN_CUSTOM_HEADER_NAME#g" \
    -e "s#@ORIGIN_CUSTOM_HEADER_VALUE@#$ORIGIN_CUSTOM_HEADER_VALUE#g" \
     Server.xml.template > Server.xml

sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" \
    -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" \
    -e "s#streamName#partnerId/$PARTNER_ID/streamName#g" \
    ./kLive/Application.xml.template > ./kLive/Application.xml

exec /sbin/entrypoint.sh
