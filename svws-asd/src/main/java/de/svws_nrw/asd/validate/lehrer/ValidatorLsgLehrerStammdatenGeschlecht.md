### ValidatorLsgLehrerStammdatenGeschlecht

**Zweig:** Lehrer-Stammdaten-Geschlecht <br>
**DTOs:** LehrerStammdaten <br>
**Ausführungsbereich-UI:** Lehrkräfte, Reiter 'Individualdaten' <br>
**Anzeigebereich-UI:** Feld 'Geschlecht' <br>
**Default-SVWS/ZeBrAS:** <br>
"svws": true, <br>
"zebras": true <br>

---

**Kürzel:** LSG00 <br>
**Altes-Kürzel:** - <br>
**SVWS/ZeBrAS:** Default <br>
**Vorbedingung:** - <br>
**Härte:** <br>
"muss": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"kann": [],<br>
"hinweis": []<br>
**Text:** Das Feld 'Geschlecht' muss besetzt sein. <br>
**Erläuterung:** - <br>
**Bedingung:** LehrerStammdaten.geschlecht = @ 

---

**Kürzel:** LSG01 <br>
**Altes-Kürzel:** AD344 <br>
**SVWS/ZeBrAS**: Default <br>
**Vorbedingung:** LSG00 <br>
**Härte:**<br>
"muss": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"kann": [],<br>
"hinweis": []
**Text:** Unzulässiger Schlüssel '" + LehrerStammdaten.geschlecht + "' im Feld 'Geschlecht'. Die gültigen Schlüssel entnehmen Sie bitte dem Pulldownmenü. <br>
**Erläuterung:** Der Prüfschritt soll anschlagen, wenn für das Feld Geschlecht ein unzulässiger Wert vorliegt. Die zulässigen Werte finden sich in der Klasse "Geschlecht.java". <br>
**Bedingung:** LehrerStammdaten.geschlecht ≠ einem Eintrag in der Klasse 'Geschlecht.java'  <br> 

---
