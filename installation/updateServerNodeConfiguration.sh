#!/bin/bash

#Get the machine IP
if [ -z "$WOWZA_EXTERNAL_IP" ]; then
        WOWZA_EXTERNAL_IP=$(curl --silent --show-error --url http://169.254.169.254/latest/meta-data/public-ipv4)
        echo "Setting WOWZA_EXTERNAL_IP as $WOWZA_EXTERNAL_IP"
fi

if [ -z "$WOWZA_INTERNAL_IP" ]; then
        WOWZA_INTERNAL_IP=$(curl --silent --show-error --url http://169.254.169.254/latest/meta-data/local-ipv4)
        echo "Setting WOWZA_INTERNAL_IP as $WOWZA_INTERNAL_IP"
fi

echo "Start update with params: partner=$PARTNER_ID, secret=$PARTNER_ADMIN_SECRET, hostName=$SERVER_NODE_HOST_NAME, serviceUrl=$SERVICE_URL, WowzaIp=$WOWZA_EXTERNAL_IP, serverTag=$SERVER_NODE_TAG"

KS=`curl -s "-dpartnerId=$PARTNER_ID&secret=$PARTNER_ADMIN_SECRET&type=2" "$SERVICE_URL/api_v3/service/session/action/start?format=1" | jq -r .`
if [ -z "$SERVER_NODE_TAG" ]; then
    FILTER="hostNameLike=$SERVER_NODE_HOST_NAME"
    SERVER_NODE_TAG="@DEFAULT_TAG@"
else 
    FILTER="tagsLike=$SERVER_NODE_TAG"
fi
echo "Set the filter as $FILTER"

RES=`curl -s "-dfilter%3AobjectType=KalturaWowzaMediaServerNodeFilter&filter%3A$FILTER&ks=$KS" "$SERVICE_URL/api_v3/service/servernode/action/list?format=1"`

for row in $(echo $RES | jq -c .objects); do
        SERVER_NODE=`echo $row | jq -r .[0]`
        if [[ ( `echo $SERVER_NODE | jq -r .hostName` == "$SERVER_NODE_HOST_NAME" ) || ( `echo $SERVER_NODE | jq -r .tags` == *"$SERVER_NODE_TAG"* ) ]]; then
                SELF_SERVER_NODE=$SERVER_NODE
                break
        fi
	
done

if [ -z "$SELF_SERVER_NODE" ]; then
	echo "Not found Server node. Exiting..."
        exit 1	
fi
echo "Got the ServerNode:"
echo $SELF_SERVER_NODE | jq .
SERVER_NODE_HOST_NAME=`echo $SELF_SERVER_NODE | jq -r .hostName`

#Getting current config and set default if not set
CONFIG=`echo $SELF_SERVER_NODE | jq -r .config`
if [ "$CONFIG" = null ]; then
        CONFIG="{}"
fi

NEW_CONF=`echo $CONFIG | jq -c --arg ip $WOWZA_EXTERNAL_IP --arg iip $WOWZA_INTERNAL_IP '.ips.primary = $ip | .ips.secondary = $ip | .ips.aws_eip = $ip | .ips.aws_int = $iip'  `
ID=`echo $SELF_SERVER_NODE | jq .id`

echo "Setting new config to ServerNodeId [$ID]"
echo $NEW_CONF | jq -c .

curl -s "-dserverNodeId=$ID&serverNode%3AobjectType=KalturaWowzaMediaServerNode&serverNode%3Aconfig=$NEW_CONF&ks=$KS" "$SERVICE_URL/api_v3/service/servernode/action/update"
echo "Done updating"

