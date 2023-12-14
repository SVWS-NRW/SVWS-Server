FROM eclipse-temurin:21
USER ContainerAdministrator
WORKDIR c:\\build
ENV NPM_CONFIG_CACHE=c:\\tmp
ADD https://svws.nrw.de/uploads/media/dockerrunner/docker-runner-win.zip C:\\tmp\\docker-runner-win.zip
RUN powershell Expand-Archive C:\\tmp\\docker-runner-win.zip -DestinationPath C:\\tmp

