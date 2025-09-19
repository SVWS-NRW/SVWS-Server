### [ ValidatorLehrerStammdatenVorname.java ](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/klehr/asd-erste-pl-spezifikation-als-markdown-erstellen/svws-asd/src/main/java/de/svws_nrw/asd/validate/lehrer/ValidatorLehrerStammdatenVorname.java?ref_type=heads)
**Schulformen:** ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"] <br>
**Fehlerhärte:** Hinweis <br>

---

**Fehlerkürzel:** LSV0 <br>
**Altes-Fehlerkürzel:** ? <br>
**Fehlertext:** Kein Wert im Feld 'vorname'. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.vorname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) = @ <br>

---

**Fehlerkürzel:** LSV1 <br>
**Altes-Fehlerkürzel:** AD33 <br>
**Fehlertext:** Vorname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs). <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.vorname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 1. Stelle = @ <br>

---

**Fehlerkürzel:** LSV2 <br>
**Altes-Fehlerkürzel:** AD33 <br>
**Fehlertext:** Vorname der Lehrkraft: Die erste Stelle des Vornamens muss mit einem Großbuchstaben besetzt sein. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.vorname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 1. Stelle = Großbuchstabe <br>

---

**Fehlerkürzel:** LSV3 <br>
**Altes-Fehlerkürzel:** AD331 <br>
**Fehlertext:** Vorname der Lehrkraft: Die zweite Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.vorname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 2. Stelle = Großbuchstabe <br>

---

**Fehlerkürzel:** LSV4 <br>
**Altes-Fehlerkürzel:** AD332 <br>
**Fehlertext:** Vorname der Lehrkraft: Die dritte Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** [LehrerStammdaten.vorname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 3. Stelle = Großbuchstabe <br>