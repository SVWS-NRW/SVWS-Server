#!/bin/sh

### Debugging ###
#export SM_LOG_LEVEL=TRACE
#echo $PWD
#ls -al
#ls -al Keylockertools-linux-x64
#cat pkcs11properties.cfg
#ls ../../artifacts_windows
### Debugging ###

# exe-Datei mit smctl und jsign signieren
echo "Signiere Windows-Installer Version $1"
Keylockertools-linux-x64/smctl sign --keypair-alias "$SM_KEYPAIR_ALIAS" --certificate "$SM_CLIENT_CERT_FILE" --config-file pkcs11properties.cfg --input "../../artifacts_windows/win64-installer-$1.exe" --tool jsign

### Debugging ###
#echo ~/.signingmanager/logs/smctl.log
#cat ~/.signingmanager/logs/smctl.log
### Debugging ###
