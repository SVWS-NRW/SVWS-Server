FROM eclipse-temurin:21

RUN apt update && apt-get -y install gettext zip

RUN mkdir -p /opt/app/svws
RUN mkdir /opt/app/svws/client
RUN mkdir /opt/app/svws/conf

COPY app /opt/app/svws
COPY conf /etc/app/svws/conf
COPY startup.sh /opt/app/svws/startup.sh
COPY init-scripts /opt/app/svws/init-scripts

RUN unzip -d /opt/app/svws/client /opt/app/svws/SVWS-Client*.zip
RUN rm -rf /opt/app/svws/SVWS-Client*.zip

WORKDIR /opt/app/svws
ENV INIT_SCRIPTS_DIR=/opt/app/svws/init-scripts SVWS_TLS_KEYSTORE_PASSWORD=test123 SVWS_TLS_KEY_ALIAS=alias1 SVWS_TLS_KEYSTORE_PATH=/etc/app/svws/conf/keystore

ENTRYPOINT ["/bin/bash", "startup.sh"]
