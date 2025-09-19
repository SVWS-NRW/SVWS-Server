
### [ValidatorLehrerStammdatenNachnamePlausibel.java](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/klehr/asd-erste-pl-spezifikation-als-markdown-erstellen/svws-asd/src/main/java/de/svws_nrw/asd/validate/lehrer/ValidatorLehrerStammdatenNachnamePlausibel.java)

**Schulformen:** ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"]<br>
**Fehlerhärte:** Hinweis <br>

---

**Fehlerkürzel:** LSN0 <br>
**Altes-Fehlerkürzel:** AD32 <br>
**Fehlertext:** Nachname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs). <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.nachname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 1. Stelle = @ <br><br>

---

**Fehlerkürzel:** LSN1 <br>
**Altes-Fehlerkürzel:** AD32 <br>
**Fehlertext:** Nachname der Lehrkraft: Die erste Stelle des Nachnamens muss - ggf. im Anschluss an einen Namenszusatz, wie z.B. \"von\" -  mit einem Großbuchstaben besetzt sein. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br>  [LehrerStammdaten.nachname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 1. Stelle ≠ Großbuchstabe <br><br>

---

**Fehlerkürzel:** LSN2 <br>
**Altes-Fehlerkürzel:** AD321 <br>
**Fehlertext:** Die zweite Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.nachname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 1. Stelle = Großbuchstabe ∧ [LehrerStammdaten.nachname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 2. Stelle = Großbuchstabe <br><br>

---

**Fehlerkürzel:** LSN3 <br>
**Altes-Fehlerkürzel:** AD322 <br>
**Fehlertext:** Nachname der Lehrkraft: Die dritte Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** <br> [LehrerStammdaten.nachname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 1. Stelle = Großbuchstabe ∧ [LehrerStammdaten.nachname](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads): 3. Stelle = Großbuchstabe <br><br>
