FROM eclipse-temurin:21

RUN apt update && apt-get -y upgrade && apt-get -y install gettext zip && apt autoclean

RUN mkdir -p /opt/app/svws/client && mkdir /opt/app/svws/conf

COPY app /opt/app/svws
COPY conf /etc/app/svws/conf
COPY startup.sh /opt/app/svws/startup.sh
COPY init-scripts /opt/app/svws/init-scripts

RUN unzip -d /opt/app/svws/client /opt/app/svws/SVWS-Client*.zip && rm -rf /opt/app/svws/SVWS-Client*.zip &&  unzip -d /opt/app/svws/adminclient /opt/app/svws/SVWS-Admin-Client*.zip && rm -rf /opt/app/svws/SVWS-Admin-Client*.zip

WORKDIR /opt/app/svws
ENV INIT_SCRIPTS_DIR=/opt/app/svws/init-scripts SVWS_TLS_KEYSTORE_PASSWORD=test123 SVWS_TLS_KEY_ALIAS=alias1 SVWS_TLS_KEYSTORE_PATH=/etc/app/svws/conf/keystore

ENTRYPOINT ["/bin/bash", "startup.sh"]
