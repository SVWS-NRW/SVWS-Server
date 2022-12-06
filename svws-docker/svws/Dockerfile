FROM eclipse-temurin:17-jre-alpine

RUN apk add gettext zip

RUN mkdir -p /opt/app/svws
RUN mkdir /opt/app/svws/client
RUN mkdir /opt/app/svws/conf
RUN mkdir /opt/app/svws/data

COPY app /opt/app/svws
COPY data /opt/app/svws/data
COPY conf /etc/app/svws/conf
COPY startup.sh /opt/app/svws/startup.sh

RUN unzip -d /opt/app/svws/client /opt/app/svws/svws-client*.zip
RUN rm -rf /opt/app/svws/svws-client*.zip

WORKDIR /opt/app/svws

ENTRYPOINT ["sh", "startup.sh"]
