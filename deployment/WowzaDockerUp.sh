VERSION=$1
SERVER_NODE_TAG=$2
SERVICE_URL=$3

INSTANCE_ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)
EC2_REGION=$(curl -s  http://169.254.169.254/latest/dynamic/instance-identity/document|grep region|awk -F\" '{print $4}')

if [ -z $SERVER_NODE_TAG ] ; then
  SERVER_NODE_TAG=$(aws ec2 describe-tags --filters "Name=resource-id,Values=$INSTANCE_ID"  --region=$EC2_REGION | jq -r '.Tags[]  | select(.Key == "DNS_Name") | .Value')
fi
if [ -z $SERVICE_URL ] ; then
  SERVICE_URL="https://${SERVER_NODE_TAG:0:2}-www.kaltura.com"
fi

echo "Setting version as: $VERSION"
echo "Setting server tag as: $SERVER_NODE_TAG"
echo "Setting service url as: $SERVICE_URL"

read -p "Are you sure? [y/n]" -n 1 -r && echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
    eval $(aws ecr get-login --registry-ids 983882572364 --no-include-email --region=eu-west-1)
    docker-compose down
    sed -e "s#@VERSION@#$VERSION#g" \
        -e "s#@SERVER_NODE_TAG@#$SERVER_NODE_TAG#g" \
        -e "s#@SERVICE_URL@#$SERVICE_URL#g" template.env > .env
    docker-compose up --no-start
    docker-compose start
fi
exit 0