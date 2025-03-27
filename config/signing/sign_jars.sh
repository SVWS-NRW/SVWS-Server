#!/bin/sh

### Debugging ###
#export SM_LOG_LEVEL=TRACE
#echo $PWD
#ls -al
#ls -al Keylockertools-linux-x64
#cat pkcs11properties.cfg
#echo $1
### Debugging ###

### JAR-Signing
#while read -r jarFilePath; do
#  # jar-Datei mit smctl und jarsigner signieren
#  echo "Signiere $jarFilePath"
#  Keylockertools-linux-x64/smctl sign --keypair-alias "$SM_KEYPAIR_ALIAS" --certificate "$SM_CLIENT_CERT_FILE" --config-file pkcs11properties.cfg --input "$jarFilePath" --tool jarsigner
#
#  # Verifikation der Signatur mit smctl und jarsigner
#  echo "Verifiziere die Signatur von $jarFilePath"
#  Keylockertools-linux-x64/smctl sign verify --verbose --input "$jarFilePath" --fingerprint "$SM_CLIENT_CERT_FINGERPRINT"
#done <"$1"
### JAR-Signing

### Debugging ###
#echo ~/.signingmanager/logs/smctl.log
#cat ~/.signingmanager/logs/smctl.log
### Debugging ###
