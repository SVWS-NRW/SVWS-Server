FROM php:8.4-apache

# Einfügen einer zus. Env-Var
ENV APACHE_DOCUMENT_ROOT=/var/www/public \
 ENMROOT=/var/www/

RUN apt-get update \
 && DEBIAN_FRONTEND=noninteractive apt-get install -y ssl-cert \
 && rm -r /var/lib/apt/lists/* \
 # neu generieren, da per default kein hostname angegeben ist
 && openssl req -subj '/CN=localhost/O=enmserver/C=DE' -new -newkey rsa:2048 -days 3650 -nodes -x509 -keyout /etc/ssl/private/ssl-cert-snakeoil.key -out /etc/ssl/certs/ssl-cert-snakeoil.pem \
 # Aktivieren der zusätzlichen Module
 && a2enmod ssl rewrite \
 # die default ssl-Site aktivieren
 && a2ensite default-ssl \
 # anpassen des Standardpfads mit html zu public bzw env-var
 && sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/sites-available/*.conf

# Setzen der zus. Konfiguration in das conf-Verzeichnis
COPY apache2.conf /etc/apache2/conf-enabled/enmserver.conf
