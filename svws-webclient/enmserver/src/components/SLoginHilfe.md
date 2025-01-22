# Anmeldung

Bei erfolgreicher Verbindung zum ENM-Client wird das Anmeldefenster angezeigt

## 1 Die Serveradresse

Hier wird die HTTPS-Adresse des Servers angegeben. Dabei muss das `https://` ausgelassen werden, es wird vom ENM-Client bei der Verbindung automatisch ergänzt. Es sind ausschließlich HTTPS-Verbindungen zum ENM-Server zulässig.

Der Client versucht schon beim ersten Aufruf in folgender Reihenfolge automatisch eine Verbindung zum Server aufzubauen:

+ Verbindung zum gleichen Host - der Server befindet sich auf der gleichen Adresse wie der Client und verwendet dessen Port, sofern vorhanden.
+ Verbindung zum gleichen Hostnamen - der Server befindet sich auf der gleichen Adresse, es wird aber der Standardport für HTTPS, Port 443, verwendet.

Sind diese Verbindungsversuche erfolglos, muss die Adresse ensprechend angepasst werden. Wird zum Beispiel ein Server unter einem anderen Port verwendet, so muss die Adresse inklusive Port angegeben werden.

Wird ein vom Client und Standardport abweichender Port verwendet, zum Beispiel, weil der Server nicht priviligiert unter Port 1443 läuft und der Client auf Port 3000, dann speichert der Client diesen Port für zukünftige Verbindungen in der sogenannten WebStorage-API des Browsers und versucht diesen Port als dritte Option bei weiteren Anmeldungen. Diese dritte Option ist nur möglich, wenn der benutzte Browser bereits eine erfolgreiche Verbindung aufbauen konnte.

Sollten mehrere Server parallel unter verschiedenen Ports laufen, wird der zuletzt benutzte Port gespeichert. Dieser kann aber bei jeder Neuanmeldung nachbearbeitet werden.

## 2 Verbinden
Nach Eingabe der Serveradresse muss mit dem Server verbunden werden. Auch nach erfolgreicher Verbindung kann die Serveradresse unter 1 bearbeitet werden und trotz ausgegrautem Verbindungsknopf neu verbunden werden.

Ist der Server nicht erreichbar, dann kommt eine Fehlermeldung dazu. Ebenso kommt eine Fehlermeldung, wenn der Server zwar erreichbar, aber keine Schemata vorhanden sind, an denen man sich anmelden kann.

## 3 DB-Schema
Hier werden alle gültigen Schemata angezeigt, die auf dem verbundenen Server zur Verfügung stehen.

## 4 Benutzername
Hier wird der Benutzername verwendet, der angemeldet werden soll und auf dem ausgewählten Schema verfügbar ist.

## 5 Passwort
Das Passwort wird verdeckt eingegeben.

## 6 Anmelden
Nach Eingabe des Benutzernamens und Passworts wird das ausgewählte Schema für die Anmeldung am Server verwendet.

## 7 Versionshinweis
Die aktuell verwendete Version des ENM-Clients wird hier angezeigt, ebenso der verwendete Commit, der als Link auf das GitHub-Repository verweist. Das Kopier-Icon an der Seite kann verwendet werden, wenn Fehlermeldungen geschrieben werden. Mit Hilfe der kopierten Daten ist die Fehlersuche für das Entwicklerteam detlich einfacher.
