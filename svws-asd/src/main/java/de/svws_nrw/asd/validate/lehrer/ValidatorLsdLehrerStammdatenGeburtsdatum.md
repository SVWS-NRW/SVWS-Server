### ValidatorLsdLehrerStammdatenGeburtsdatum

**Zweig:** Lehrer-Stammdaten-Geburtsdatum <br>
**DTOs:** LehrerStammdaten <br>
**Ausführungsbereich-UI:** Lehrkräfte, Reiter 'Individualdaten' <br>
**Anzeigebereich-UI:** Feld 'Geburtsdatum' <br>
**Default-SVWS/ZeBrAS:** <br>
"svws": true, <br>
"zebras": true, <br>
**Default-Fehlerhärte:**<br>
"muss": [],<br>
"kann": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"hinweis": []

---

**Kürzel:** LSD00 <br>
**Altes-Kürzel:** - <br>
**SVWS/ZeBrAS:** Default <br>
**Vorbedingung:** - <br>
**Härte:** <br>
"muss": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"kann": [],<br>
"hinweis": []<br>
**Text:** Das Feld 'Geburtsdatum' muss besetzt sein. <br>
**Erläuterung:** - <br>
**Bedingung:** LehrerStammdaten.geburtsdatum = @ 

---

**Fehlerkürzel:** LSD01 <br>
**Altes-Fehlerkürzel:** AD341, AD342 und AD343 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** <br>
"muss": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"kann": [],<br>
"hinweis": []<br>
**Fehlertext:** Das Geburtsdatum ist ungültig: <br>
**Erläuterung:** - <br>
**Bedingung:** (Im Java Code wird das Geburtsdatum zur Prüfung an den DateManager übergeben. Deswegen können wir die genaue Bedingung dazu nicht spezifizieren)

---

**Fehlerkürzel:** LSD10 <br>
**Altes-Fehlerkürzel:** AD343 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br>
**Fehlertext:** Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind die Werte {aktuellesSchuljahr} - 80 bis {aktuellesSchuljahr} - 18}. <br>
**Erläuterung:** Die zulässigen Werte liegen zwischen dem aktuellen Schuljahr minus 80 und dem aktuellen Schuljahr minus 18. <br>
**Bedingung:** ¬ LehrerStammdaten.geburtsdatum (format: JJJJ) > aktuellesSchuljahr - 80 ∧ LehrerStammdaten.geburtsdatum (format: JJJJ) < aktuellesSchuljahr - 18 <br>

---