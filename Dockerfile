#docker build -t kaltura/media-server .
#docker stop $(docker ps -a -q) && docker rm $(docker ps -a -q)
#docker run -p 1935:1935 -p 8087:8087 --name wowza_instance -t kaltura/media-server
#docker exec -it `docker ps | grep "media-server" | awk '{print $1}' ` bash

ARG WowzaVersion=4.7.6

#create baseline
FROM  wowzamedia/wowza-streaming-engine-linux:$WowzaVersion AS baseWowza


#build a build machine
FROM java:8-jdk AS build

ENV PATH ${PATH}:/usr/local/gradle-2.12/bin

RUN apt-get update &&  apt-get install unzip

# Install gradle
WORKDIR /usr/local
RUN wget https://services.gradle.org/distributions/gradle-2.12-bin.zip && \
    unzip gradle-2.12-bin.zip && \
    rm -f gradle-2.12-bin.zip


## deploy code
WORKDIR  /usr/local/source

# copy all jars from wowza docker
COPY --from=baseWowza   /usr/local/WowzaStreamingEngine/lib /opt/local/WowzaStreamingEngine/lib

#copy all source code
COPY ./KalturaWowzaServer ./KalturaWowzaServer
COPY ./build.gradle ./build.gradle
COPY ./settings.gradle ./settings.gradle

# build
ARG JarVersion=1.0.0
RUN gradle -Pversion=$JarVersion prepareRelease


# create the actual docker
FROM  wowzamedia/wowza-streaming-engine-linux:$WowzaVersion

MAINTAINER guy.jacubovski@kaltura.com

# for debug and scripts
RUN apt-get update && apt-get -y install less vim htop curl jq

ENV SERVICE_URL https://www.kaltura.com
ENV PARTNER_ID -5
ENV PARTNER_ADMIN_SECRET XXX
ENV SERVER_NODE_HOST_NAME @HOST_NAME@
ENV NVIDIA_VISIBLE_DEVICES all
ENV NVIDIA_DRIVER_CAPABILITIES compute,video,utility

EXPOSE 1935/tcp 8086/tcp 8087/tcp 8088/tcp 554/tcp

VOLUME ["/var/log/wowza"]

#remove unwanted stuff from images
RUN rm -rf /usr/local/WowzaStreamingEngine/content/ && \
    rm -rf /usr/local/WowzaStreamingEngine/documentation/ && \
    rm -rf /usr/local/WowzaStreamingEngine/examples/ && \
    rm -rf /usr/local/WowzaStreamingEngine/*.html && \
    rm -rf /var/lib/apt/lists/*


WORKDIR  /usr/local/WowzaStreamingEngine/lib

#copy build artifacts from build machine
COPY --from=build /usr/local/source/KalturaWowzaServer/build/tmp/artifacts/* ./

#create symlinks
RUN rm -f KalturaClientLib.jar && \
    rm -f KalturaWowzaServer.jar && \
    ln -s KalturaClientLib-*.jar KalturaClientLib.jar && \
    ln -s KalturaWowzaServer-*.jar KalturaWowzaServer.jar



# copy configuration
WORKDIR  /usr/local/WowzaStreamingEngine/conf
COPY ./installation/configTemplates/.   ./
COPY ./installation/kalturaEntryPoint.sh   /sbin/
COPY ./installation/updateServerNodeConfiguration.sh   /sbin/
#COPY ./installation/configTemplates/templates/HD_plus.xml /usr/local/WowzaStreamingEngine/transcoder/templates


ENTRYPOINT ["/sbin/kalturaEntryPoint.sh"]
