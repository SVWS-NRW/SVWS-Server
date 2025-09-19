
### [ValidatorLehrerStammdatenNachnameVorhanden.java](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/klehr/asd-erste-pl-spezifikation-als-markdown-erstellen/svws-asd/src/main/java/de/svws_nrw/asd/validate/lehrer/ValidatorLehrerStammdatenNachnameVorhanden.java)

**Schulformen:** ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"]<br>
**Fehlerhärte:** Muss <br>

---

**Fehlerkürzel:** LSNC0 <br>
**Altes-Fehlerkürzel:** AC2? (muss noch überprüft werden) <br>
**Fehlertext:** Kein Wert im Feld 'nachname'. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.nachnamevorhanden](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) = @ <br>

---

**Neues-Fehlerkürzel:** LSNC1 <br>
**Altes-Fehlerkürzel:** - <br>
**Fehlertext:** Das Feld 'nachname' darf nicht nur aus Leerzeichen bestehen. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.nachnamevorhanden](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) = Leerzeichen <br>

---
