# Installation and deployment of Media-Server on AWS machine

Create new ubuntu machine in the AWS cloud - make sure to open ports 80, 1935, 554, 8086, 8087 (by adding inbound role). Notice to open 8086 and 8087 only from IP as your Live-Controller server.
In order to maximize your service capability use machine with GPU (as g3.4xlarge for example) with AMI of "Deep Learning AMI (Amazon Linux) Version 18.0"

## Machine installation:
- Follow instructions
https://docs.aws.amazon.com/batch/latest/userguide/batch-gpu-ami.html
	
- Increase TCP parameters:
	1. sudo sysctl -w net.core.rmem_max=16777216
	2. sudo sysctl -w net.core.wmem_max=16777216
	3. sudo sysctl -w net.ipv4.tcp_wmem="16384 4194304 16777216"
	4. sudo sysctl -w net.ipv4.tcp_rmem="16384 4194304 16777216"
	
## AWS deployment:
1. copy docker-compose [file](../deployment/docker-compose.yml) to your machine
2. copy [env template file](../deployment/template.env) to your machine and rename it as ".env"
3. Fill all env variable in the .env file
4. Run `docker-compose up --no-start` and then `docker-compose start`
