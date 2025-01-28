#!/bin/sh

# package default-jre-headless installieren
apt-get update
apt-get install --fix-missing
apt-get -y install default-jre-headless

# jsign 7.0 downloaden und installieren
curl -fSslL https://github.com/ebourg/jsign/releases/download/7.0/jsign_7.0_all.deb -o jsign_7.0_all.deb
dpkg --install jsign_7.0_all.deb

# Zertifikatsdatei Base64 dekodieren
echo "$SM_CLIENT_CERT_FILE_B64" | base64 -d > Certificate_pkcs12.p12

# Digicert-Keylockertools herunterladen und entpacken
curl -X GET https://one.digicert.com/signingmanager/api-ui/v1/releases/Keylockertools-linux-x64.tar.gz/download -H "x-api-key:$SM_API_KEY_DOWNLOAD" -o Keylockertools-linux-x64.tar.gz
# Entpacken der Keylockertools
tar -xvf Keylockertools-linux-x64.tar.gz
# Execution Rechte für smctl tool setzen
chmod u+x Keylockertools-linux-x64/smctl

echo "name=signingmanager" > pkcs11properties.cfg
echo "library=\"$PWD/Keylockertools-linux-x64/smpkcs11.so\"" >> pkcs11properties.cfg
echo "slotListIndex=0" >> pkcs11properties.cfg

### Debugging ###
# Healthcheck für Authentifizierung
#Keylockertools-linux-x64/smctl healthcheck
#ls -al
#cat pkcs11properties.cfg
### Debugging ###
