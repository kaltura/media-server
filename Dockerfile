#docker build -t kaltura-media-server .
#docker rm $(docker ps -a -q)
#docker  run   -p 1935:1935 -p 8087:8087 --name  wowza_instance -t kaltura-media-server


ARG WowzaVersion=4.7.3

FROM  wowzamedia/wowza-streaming-engine-linux:$WowzaVersion

MAINTAINER guy.jacubovski@kaltura.com

ENV SERVICE_URL https://www.kaltura.com
ENV PARTNER_ID 1802381
ENV PARTNER_ADMIN_SECRET ef16b1026d6960e6661990187d84fe47
ENV WOWZA_LOG_DIR=/var/log/wowza

EXPOSE 1935/tcp 8086/tcp 8087/tcp 8088/tcp 554/tcp

VOLUME ["${WOWZA_LOG_DIR}"]

## deploy code
# copy binary file
WORKDIR  /usr/local/WowzaStreamingEngine/lib
COPY ./KalturaWowzaServer/build/tmp/artifacts/* ./

#create symlinks
RUN ln -s KalturaClientLib-*.jar KalturaClientLib.jar && \
    ln -s KalturaWowzaServer-*.jar KalturaWowzaServer.jar



# copy configuration
WORKDIR  /usr/local/WowzaStreamingEngine/conf
COPY ./installation/configTemplates/.   ./
COPY ./installation/kalturaEntryPoint.sh   /sbin/

ENTRYPOINT ["/sbin/kalturaEntryPoint.sh"]
