#docker build -t kaltura-media-server .
#docker rm $(docker ps -a -q)
#docker  run -it  -p 1935:1935 -p 8087:8087 --name  wowza_instance -t kaltura-media-server


ARG WowzaVersion=4.7.3

FROM  wowzamedia/wowza-streaming-engine-linux:$WowzaVersion

MAINTAINER guy.jacubovski@kaltura.com

ENV SERVICE_URL https://www.kaltura.com
ENV PARTNER_ID -5
ENV PARTNER_ADMIN_SECRET 123
ENV WOWZA_LOG_DIR=/var/log/wowza

EXPOSE 1935/tcp 8086/tcp 8087/tcp 8088/tcp 554/tcp

#VOLUME ["${WOWZA_LOG_DIR}"]


# copy binary file
WORKDIR  /usr/local/WowzaStreamingEngine/lib
COPY ./KalturaWowzaServer/build/tmp/artifacts/* ./
#create symlinks
RUN ln -s KalturaClientLib-*.jar KalturaClientLib.jar
RUN ln -s KalturaWowzaServer-*.jar KalturaWowzaServer.jar
#TBD
RUN mkdir /usr/local/WowzaStreamingEngine/applications/kLive


# copy configuration
WORKDIR  /usr/local/WowzaStreamingEngine/conf
COPY ./installation/configTemplates/.   ./

# replace config
RUN sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" -e "s#@KALTURA_PARTNER_ADMIN_SECRET@#$PARTNER_ADMIN_SECRET#g" Server.xml.template > Server.xml
RUN sed -e "s#@KALTURA_SERVICE_URL@#$SERVICE_URL#g" -e "s#@KALTURA_PARTNER_ID@#$PARTNER_ID#g" -e "s#@KALTURA_PARTNER_ADMIN_SECRET@#$PARTNER_ADMIN_SECRET#g" ./kLive/Application.xml.template > ./kLive/Application.xml

# set log folder
RUN mv log4j.properties log4j.properties.template
RUN sed -e "s#/var/log/#$WOWZA_LOG_DIR/#g" log4j.properties.template > log4j.properties



ENTRYPOINT ["/sbin/entrypoint.sh"]
