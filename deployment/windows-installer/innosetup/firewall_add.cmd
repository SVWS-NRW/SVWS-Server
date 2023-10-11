rem Open Port 443

netsh advfirewall firewall show rule name="SVWS-Server incoming TCP Port 443" >nul
if not ERRORLEVEL 1 (
echo Firewall-Regel "SVWS-Server incoming TCP Port 443" existiert bereits
) else (
netsh advfirewall firewall add rule name="SVWS-Server incoming TCP Port 443" dir=in action=allow protocol=TCP localport=443
)

netsh advfirewall firewall show rule name="SVWS-Server outgoing TCP Port 443" >nul
if not ERRORLEVEL 1 (
echo Firewall-Regel "SVWS-Server outgoing TCP Port 443" existiert bereits
) else (
netsh advfirewall firewall add rule name="SVWS-Server outgoing TCP Port 443" dir=out action=allow protocol=TCP localport=443
)
