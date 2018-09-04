# Installation and deployment of Media-Server on AWS machine

Create new ubuntu machine in the AWS cloud - make sure to open ports 80, 1935, 554, 8086, 8087 (by adding inbound role)
In order to maximize your service capability use machine with GPU (as g3.4xlarge for example) with AMI of "Deep Learning Base AMI (Ubuntu) Version 8.0"

## Machine installation:
- For GPU machine only run the following:
    1. wget https://developer.download.nvidia.com/compute/cuda/repos/ubuntu1604/x86_64/cuda-repo-ubuntu1604_9.1.85-1_amd64.deb
	2. sudo dpkg --install cuda-repo-ubuntu1604_9.1.85-1_amd64.deb
	3. sudo apt-key adv --fetch-keys https://developer.download.nvidia.com/compute/cuda/repos/ubuntu1604/x86_64/7fa2af80.pub
	4. sudo apt install gnupg-curl
	5. sudo apt install cuda
	6. export PATH="/usr/local/cuda-9.1/bin:$PATH"
	7. export LD_LIBRARY_PATH="/usr/local/cuda-9.1/lib64:$LD_LIBRARY_PATH"
	8. Verify GPU with: "nvidia-smi -q -d UTILIZATION"
	
- Install Docker:
	1. curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
	2. sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
	3. sudo apt-get update
	4. sudo apt-get install docker-ce
	5. sudo usermod -aG docker $USER
	
- Install Docker-compose:
	1. apt install docker-compose
	2. sudo curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
	3. sudo chmod +x /usr/local/bin/docker-compose
	4. sudo ln /usr/local/bin/docker-compose /usr/bin
	
- Install nvidia docker:
	1. curl -s -L https://nvidia.github.io/nvidia-docker/gpgkey | sudo apt-key add -
	2. distribution=$(. /etc/os-release;echo $ID$VERSION_ID)
	3. curl -s -L https://nvidia.github.io/nvidia-docker/$distribution/nvidia-docker.list | sudo tee /etc/apt/sources.list.d/nvidia-docker.list
	4. sudo apt-get update
	5. sudo apt-get install -y nvidia-docker2
	6. sudo pkill -SIGHUP dockerd
	7. Verify with: "docker run --runtime=nvidia --rm nvidia/cuda nvidia-smi"
	
- Increase TCP parameters:
	1. sudo sysctl -w net.core.rmem_max=16777216
	2. sudo sysctl -w net.core.wmem_max=16777216
	3. sudo sysctl -w net.ipv4.tcp_wmem="16384 4194304 16777216"
	4. sudo sysctl -w net.ipv4.tcp_rmem="16384 4194304 16777216"
	
## AWS deployment:
	1. copy docker-compose [file](../deployment/docker-compose.yml) to your machine
	2. copy [env template file](../deployment/template.env) to your machine and rename it as ".env"
	3. Fill all env variable in the .env file
	4. docker-compose create
	5. docker-compose up
