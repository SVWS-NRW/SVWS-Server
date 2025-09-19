
### [ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum.java](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/validate/lehrer/ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum.java?ref_type=heads)

**Schulformen:** ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"]<br>
**Fehlerhärte:** Hinweis <br>

---

**Fehlerkürzel:** LAR0 <br>
**Altes-Fehlerkürzel:** - <br>
**Fehlertext:** Kein Wert im Feld 'rechtsverhaeltnis'. <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:** [LehrerPersonalabschnittsdaten.rechtsverhaeltnis](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) = @

---

**Fehlerkürzel:** LAR1 <br>
**Altes-Fehlerkürzel:** BB <br>
**Fehlertext:** Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Lebenszeit (Rechtsverhältnis = L)" + " zwischen " + {aktuellesSchuljahr - ?} + " und " + {aktuellesSchuljahr - ?} + " liegen. Bitte prüfen!
 <br>
**Erläuterung:** <br>
**Bedingung:** 
if [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) >= 1946:
[LehrerPersonalabschnittsdaten.rechtsverhaeltnis](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) = 'L'  <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) < aktuellesSchuljahr - 55  <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) > aktuellesSchuljahr - 20  <br>

---

**Fehlerkürzel:** LAR2 <br>
**Altes-Fehlerkürzel:** BB <br>
**Fehlertext:** Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Probe (Rechtsverhältnis = P)" + " zwischen " {aktuellesSchuljahr - 55} " und " {aktuellesSchuljahr - 20} " liegen. Bitte prüfen! <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:**  [LehrerPersonalabschnittsdaten.rechtsverhaeltnis](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) = 'P'  <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) < aktuellesSchuljahr - 55  <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) > aktuellesSchuljahr - 20  <br>

---

**Fehlerkürzel:** LAR3 <br>
**Altes-Fehlerkürzel:** BB <br>
**Fehlertext:** Der Wert für das Geburtsjahr sollte bei Lehramtsanwärtern/-innen (Rechtsverhältnis = W)" + " zwischen " + {aktuellesSchuljahr - 50} + " und " + {aktuellesSchuljahr - 18} + " liegen. Bitte prüfen!" <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:**  [LehrerPersonalabschnittsdaten.rechtsverhaeltnis](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) = 'W'  <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) < aktuellesSchuljahr - 50  <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) > aktuellesSchuljahr - 18  <br>

---

**Fehlerkürzel:** LAR4 <br>
**Altes-Fehlerkürzel:** BB <br>
**Fehlertext:** Der Wert für das Geburtsjahr sollte bei sonstigen Rechtsverhältnissen" + " zwischen " + {aktuellesSchuljahr - 80} + " und " + {aktuellesSchuljahr - 18} + " liegen. Bitte prüfen!
 <br>
**Erläuterung:** Siehe Fehlertext <br>
**Bedingung:**  [LehrerPersonalabschnittsdaten.rechtsverhaeltnis](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) ≠ 'L','P','W' <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) < aktuellesSchuljahr - 80  <br>
∧ [LehrerStammdaten.geburtsdatum](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java?ref_type=heads) (Format: YYYY) > aktuellesSchuljahr - 18  <br>
