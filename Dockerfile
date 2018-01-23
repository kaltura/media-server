#docker build -t kaltura/media-server .
#docker rm $(docker ps -a -q)
#docker  run   -p 1935:1935 -p 8087:8087 --name  wowza_instance -t kaltura/media-server
#docker exec -it `docker ps | grep "media-server" | awk '{print $1}' ` bash

ARG WowzaVersion=4.7.3
ARG JarVersion=1.2.3

#create baseline
FROM  wowzamedia/wowza-streaming-engine-linux:$WowzaVersion AS baseWowza


#build a build machine
FROM java:7-jdk AS build

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
COPY ./ ./

# build
RUN gradle -Pversion=$JarVersion prepareRelease


# create the actual docker
FROM  wowzamedia/wowza-streaming-engine-linux:$WowzaVersion

MAINTAINER guy.jacubovski@kaltura.com

ENV SERVICE_URL https://www.kaltura.com
ENV PARTNER_ID -5
ENV PARTNER_ADMIN_SECRET XXX
ENV WOWZA_LOG_DIR /var/log/wowza

EXPOSE 1935/tcp 8086/tcp 8087/tcp 8088/tcp 554/tcp

VOLUME ["${WOWZA_LOG_DIR}"]

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


ENTRYPOINT ["/sbin/kalturaEntryPoint.sh"]
