FROM eclipse-temurin:21

RUN apt update && apt-get -y install gettext zip mariadb-client

RUN mkdir -p /opt/app/svws
RUN mkdir /opt/app/svws/client
RUN mkdir /opt/app/svws/conf

COPY /build/tmp/extLibs /opt/app/svws
COPY /svws/conf /etc/app/svws/conf
COPY /svws/startup.sh /opt/app/svws/startup.sh
COPY /build/dockerimage/init-scripts /etc/app/svws/init-scripts
RUN unzip -d /opt/app/svws/client /opt/app/svws/SVWS-Client.zip
RUN rm -rf /opt/app/svws/svws-client*.zip

WORKDIR /opt/app/svws

ENTRYPOINT ["/bin/bash", "startup.sh"]
