#!/bin/bash

#Get the machine IP
if [ -z "$WOWZA_EXTERNAL_IP" ]; then
        WOWZA_EXTERNAL_IP=$(curl --silent --show-error --url http://169.254.169.254/latest/meta-data/public-ipv4)
        echo "Setting WOWZA_EXTERNAL_IP as $WOWZA_EXTERNAL_IP"
fi

echo "Start update with params: partner=$PARTNER_ID, secret=$PARTNER_ADMIN_SECRET, hostName=$SERVER_NODE_HOST_NAME, serviceUrl=$SERVICE_URL, WowzaIp=$WOWZA_EXTERNAL_IP"

KS=`curl "-dpartnerId=$PARTNER_ID&secret=$PARTNER_ADMIN_SECRET&type=2" "$SERVICE_URL/api_v3/service/session/action/start?format=1" | jq -r .`
RES=`curl "-dfilter%3AobjectType=KalturaWowzaMediaServerNodeFilter&filter%3AhostNameLike=$SERVER_NODE_HOST_NAME&ks=$KS" "$SERVICE_URL/api_v3/service/servernode/action/list?format=1"`

for row in $(echo $RES | jq -c .objects); do
        SERVER_NODE=`echo $row | jq -r .[0]`
        if [[ `echo $SERVER_NODE | jq -r .hostName` == "$SERVER_NODE_HOST_NAME" ]]; then
                SELF_SERVER_NODE=$SERVER_NODE
                break
        fi
done
echo "Got the ServerNode:"
echo $SELF_SERVER_NODE | jq .

#Getting current config and set default if not set
CONFIG=`echo $SELF_SERVER_NODE | jq -r .config`
if [ "$CONFIG" = null ]; then
        CONFIG='{"ips":"{}"}'
else
	CURRENT_IPS=`echo $CONFIG | jq .ips`
	if  [ $CURRENT_IPS = null ] || [ -n $CURRENT_IPS ]; then
        	CONFIG=`echo $CONFIG | jq '. + {ips: {}}'`
	fi
fi

NEW_IPS=`echo $CONFIG | jq .ips -r | jq -c --arg ip $WOWZA_EXTERNAL_IP ' . |= . + {primary: $ip, secendary: $ip}' `
NEW_CONF=`echo $CONFIG | jq -c --arg new_ips "$NEW_IPS" '. + {ips:$new_ips}' `
ID=`echo $SELF_SERVER_NODE | jq .id`

echo "Setting new config to ServerNodeId [$ID]"
echo $NEW_CONF | jq -c .

curl "-dserverNodeId=$ID&serverNode%3AobjectType=KalturaWowzaMediaServerNode&serverNode%3Aconfig=$NEW_CONF&ks=$KS" "$SERVICE_URL/api_v3/service/servernode/action/update"
echo "Done updating"

