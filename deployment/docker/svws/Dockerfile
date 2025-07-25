FROM eclipse-temurin:21

RUN apt update && apt-get -y upgrade && apt-get -y install gettext zip && apt autoclean

RUN mkdir -p /opt/app/svws/client && mkdir /opt/app/svws/conf && mkdir /opt/app/svws/init

COPY app /opt/app/svws
COPY svwsconfig-template.json /opt/app/svws/templates/svwsconfig-template.json
COPY svwsconfig-template-nodb.json /opt/app/svws/templates/svwsconfig-template-nodb.json
COPY startup.sh /opt/app/svws/startup.sh

RUN unzip -d /opt/app/svws/client /opt/app/svws/SVWS-Client*.zip && rm -rf /opt/app/svws/SVWS-Client*.zip && unzip -d /opt/app/svws/adminclient /opt/app/svws/SVWS-Admin-Client*.zip && rm -rf /opt/app/svws/SVWS-Admin-Client*.zip

WORKDIR /opt/app/svws
ENV SVWS_TLS_KEYSTORE_PASSWORD=test123 SVWS_TLS_KEY_ALIAS=alias1 SVWS_TLS_KEYSTORE_PATH=/opt/app/svws/conf SVWS_TLS_CERT_CN=SVWSCERT SVWS_TLS_CERT_OU=School SVWS_TLS_CERT_O=School SVWS_TLS_CERT_L=Duesseldorf SVWS_TLS_CERT_S=NRW SVWS_TLS_CERT_C=Germany

ENTRYPOINT ["/bin/bash", "startup.sh"]
