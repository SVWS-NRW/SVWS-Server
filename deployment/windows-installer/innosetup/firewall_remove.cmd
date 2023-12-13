rem Remove TCP Port 443
netsh advfirewall firewall delete rule name="SVWS-Server incoming TCP Port 443"
netsh advfirewall firewall delete rule name="SVWS-Server outgoing TCP Port 443"