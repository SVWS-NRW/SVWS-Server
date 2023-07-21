#!/bin/bash

keytool -genkey -noprompt -alias ${SVWS_TLS_KEY_ALIAS} -dname "CN=test, OU=test, O=test, L=test, S=test, C=test" -keystore ${SVWS_TLS_KEYSTORE_PATH}/keystore -storepass ${SVWS_TLS_KEYSTORE_PASSWORD} -keypass ${SVWS_TLS_KEYSTORE_PASSWORD}  -keyalg RSA