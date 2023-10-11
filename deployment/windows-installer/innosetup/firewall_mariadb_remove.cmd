rem Remove Port for MariaDB-Server (needed for Schild3)
netsh advfirewall firewall delete rule name="SVWS-Server incoming MariaDB Port"
netsh advfirewall firewall delete rule name="SVWS-Server outgoing MariaDB Port"