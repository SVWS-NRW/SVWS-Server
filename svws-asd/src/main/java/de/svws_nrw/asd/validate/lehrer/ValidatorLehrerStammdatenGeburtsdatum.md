### [ValidatorLehrerStammdatenGeburtsdatum.java ](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/validate/lehrer/ValidatorLehrerStammdatenGeburtsdatum.java?ref_type=heads)
**Schulformen:** ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "GY", "WB", "BK", "SR", "SG", "SB"]<br>
**Fehlerhärte:** Kann <br>

---

**Fehlerkürzel:** LSG1 <br>
**Altes-Fehlerkürzel:** AD343 <br>
**Fehlertext:** Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind die Werte {aktuellesSchuljahr} - 80 bis {aktuellesSchuljahr} - 18}. <br>
**Erläuterung:** Die zulässigen Werte liegen zwischen dem aktuellen Schuljahr minus 80 und dem aktuellen Schuljahr minus 18. <br>
**Bedingung:** ¬ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (format: JJJJ) > aktuellesSchuljahr - 80 ∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (format: JJJJ) < aktuellesSchuljahr - 18 <br>

---