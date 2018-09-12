#!/bin/bash
VERSION=$1
SERVER_NODE_TAG=$2
if [ -z $SERVER_NODE_TAG ]
then
    SERVER_NODE_TAG=$(aws ec2 describe-tags --filters "Name=resource-id,Values=$(ec2metadata --instance-id)" | jq -r '.Tags[]  | select(.Key == "Name") | .Value')
fi

echo "Setting version as: $VERSION"
echo "Setting server tag as: $SERVER_NODE_TAG"

read -p "Are you sure? [y/n]" -n 1 -r && echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
    docker-compose down
    sed -i "s/@VERSION@/$VERSION/g" .env
    sed -i "s/@SERVER_NODE_TAG@/$SERVER_NODE_TAG/g" .env
    docker-compose up --no-start
    docker-compose start
fi

