## Anmeldung

Ist die Verbindung zum SVWS-Server über den SVWS-Webclient möglich, bekommen Sie dieses Anmeldefenster gezeigt. 

### Die Serveradresse

Geben Sie die Adresse des Servers ein, dabei muss das "**https://**" ausgelassen werden, dieses wird automatisch ergänzt. Geben Sie für eine lokale Installation **localhost** ein und in einer Netzwerkumgebung die IP- oder Text-Adresse ein, die Sie von Ihrer IT erhalten haben. Wurde der Port verändert, ist dieser als *Adresse:Port* anzugeben

Der Client versucht ersten Aufruf in folgender Reihenfolge automatisch eine Verbindung zum Server aufzubauen:
* Verbindung zum gleichen Host - der Server befindet sich auf der gleichen Adresse wie der Client und verwendet dessen Port, sofern vorhanden.
* Verbindung zum gleichen Hostnamen - der Server befindet sich auf der gleichen Adresse, es wird aber der Standardport für HTTPS, Port 443, verwendet. Sind diese Verbindungsversuche erfolglos, muss die Adresse entsprechend angepasst werden. Wird zum Beispiel ein Server unter einem anderen Port verwendet, so muss die Adresse inklusive Port angegeben werden, zum Beispiel *localhost:444*.
* Wird ein vom Client und Standardport abweichender Port verwendet, zum Beispiel, weil der Server nicht priviligiert unter Port 1443 läuft und der Client auf Port 3000, dann speichert der Client diesen Port für zukünftige Verbindungen in der sogenannten WebStorage-API des Browsers und versucht diesen Port als dritte Option bei weiteren Anmeldungen. Diese dritte Option ist nur möglich, wenn der benutzte Browser bereits eine erfolgreiche Verbindung aufbauen konnte.

**Hinweis:** Sollten *mehrere Server parallel unter verschiedenen Ports* laufen, wird der zuletzt benutzte Port gespeichert. Dieser kann aber bei jeder Neuanmeldung nachbearbeitet werden.

### Verbinden

Nach Eingabe der Serveradresse kann manuell mit dem Server verbunden werden. Auch nach erfolgreicher Verbindung kann die Serveradresse unter 1 bearbeitet werden und trotz ausgegrautem Verbindungsknopf neu verbunden werden.

Ist der Server nicht erreichbar, dann kommt eine Fehlermeldung dazu. Ebenso kommt eine Fehlermeldung, wenn der Server zwar erreichbar, aber keine Schemata vorhanden sind, an denen man sich anmelden kann.

Wollen Sie sich an dem Server mit Anmeldedaten - siehe unten - als Nutzer anmelden, ist ein gesonderter Klick auf *Verbinden* nicht notwendig.

### DB-Schema

Hier werden alle gültigen Schemata, also die "tatsächlichen Datenbanken" angezeigt, die auf dem verbundenen Server zur Verfügung stehen. Wählen Sie Ihr Schema aus. 

### Benutzername und Passwort
Hier wird der Benutzername verwendet mit dem Sie sich auf dem ausgewählten Schema anmelden möchten. Dieser Nutzername muss vorhanden sein. 

Das Passwort wird verdeckt eingegeben.

Klicken Sie nun auf  **Anmelden**.

### Versionshinweis

Die aktuell verwendete Version des SVWS-Clients wird hier angezeigt, ebenso der verwendete Commit, der als Link auf das GitHub-Repository verweist.

Das Kopier-Icon <span class="icon-sm i-ri-file-copy-line" /> neben der Versionsnummer kann verwendet werden, um Fehlermeldungen in die Zwischenablage zu kopieren, diese Fehlermeldung kann dem Entwicklerteam übermittelt werden und hilft deutlich bei einer Fehlersuche.

Über die Links **Impressum** und **Datenschutz** werden eben diese Informationen aufgerufen.

---

Nutzen Sie für weitere Informationen und Anleitungen die [ausführliche Dokumentation](https://doku.svws-nrw.de).
