FROM eclipse-temurin:17

RUN apt update && apt-get -y install gettext zip

RUN mkdir -p /opt/app/svws
RUN mkdir /opt/app/svws/client
RUN mkdir /opt/app/svws/conf

COPY app /opt/app/svws
COPY conf /etc/app/svws/conf
COPY startup.sh /opt/app/svws/startup.sh

RUN unzip -d /opt/app/svws/client /opt/app/svws/SVWS-Client*.zip
RUN rm -rf /opt/app/svws/svws-client*.zip

WORKDIR /opt/app/svws

ENTRYPOINT ["/bin/bash", "startup.sh"]
