rem Open Port for MariaDB-Server (needed for Schild3)

netsh advfirewall firewall show rule name="SVWS-Server incoming MariaDB Port" >nul
if not ERRORLEVEL 1 (
echo Firewall-Regel "SVWS-Server incoming MariaDB Port" existiert bereits
) else (
netsh advfirewall firewall add rule name="SVWS-Server incoming MariaDB Port" dir=in action=allow protocol=TCP localport=%1
)

netsh advfirewall firewall show rule name="SVWS-Server outgoing MariaDB Port" >nul
if not ERRORLEVEL 1 (
echo Firewall-Regel "SVWS-Server outgoing MariaDB Port" existiert bereits
) else (
netsh advfirewall firewall add rule name="SVWS-Server outgoing MariaDB Port" dir=out action=allow protocol=TCP localport=%1
)
