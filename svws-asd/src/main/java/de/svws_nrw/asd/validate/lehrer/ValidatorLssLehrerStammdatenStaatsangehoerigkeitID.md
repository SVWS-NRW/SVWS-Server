### ValidatorLssLehrerStammdatenStaatsangehoerigkeitID

**Zweig:** Lehrer-Stammdaten-StaatsangehoerigkeitID <br>
**DTOs:** LehrerStammdaten <br>
**Ausführungsbereich-UI:** Lehrkräfte, Reiter 'Individualdaten' <br>
**Anzeigebereich-UI:** Feld 'Staatsangehörigkeit' <br>
**Default-SVWS/ZeBrAS:** <br>
"svws": true, <br>
"zebras": true <br>

---

**Kürzel:** LSS00 <br>
**Altes-Kürzel:** - <br>
**SVWS/ZeBrAS:** Default <br>
**Vorbedingung:** - <br>
**Härte:** <br>
"muss": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"kann": [],<br>
"hinweis": []<br>
**Text:** Das Feld 'Staatsangehörigkeit' muss besetzt sein. <br>
**Erläuterung:** - <br>
**Bedingung:** LehrerStammdaten.staatsangehoerigkeitID = @ <br>

---

**Kürzel:** LSS01 <br>
**Altes-Kürzel:** - <br>
**SVWS/ZeBrAS:** Default <br>
**Vorbedingung:** LSS00 <br>
**Härte:** <br>
"muss": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"kann": [],<br>
"hinweis": [] <br>
**Text:** Das Feld 'Staatsangehörigkeit' muss zulässig sein. <br>
**Erläuterung:** - <br>
**Bedingung:** LehrerStammdaten.staatsangehoerigkeitID ≠ einem Eintrag in Nationalitaeten.json <br>

---

**Kürzel:** LSS10 <br>
**Altes-Kürzel:** - <br>
**SVWS/ZeBrAS:** Default <br>
**Vorbedingung:** LSS00, LSS01 <br>
**Härte:** <br>
"muss": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"kann": [],<br>
"hinweis": [] <br>
**Text:** Der eingetragene Wert für das Feld 'Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen. <br>
**Erläuterung:** Der eingetragene Katalogwert ist gültig, wenn gueltigVon kleiner als das aktuelle Schuljahr ist oder NULL ist, oder wenn gueltigBis größer als das aktuelle Schuljahr ist oder NULL ist. <br>
**Bedingung:** <br>
((LehrerStammdaten.staatsangehoerigkeitID.bezeichner.gueltigVon ≠ NULL) <br>
∧(LehrerStammdaten.staatsangehoerigkeitID.bezeichner.gueltigVon > aktuellesSchuljahr ))<br>
∨((LehrerStammdaten.staatsangehoerigkeitID.bezeichner.gueltigBis ≠ NULL) <br>
∧(LehrerStammdaten.staatsangehoerigkeitID.bezeichner.gueltigBis < aktuellesSchuljahr)) <br>

---

Umsetzung muss noch geklärt werden
**Fehlerkürzel:** LSS11 <br>
**Altes-Kürzel:** BD <br>
**SVWS/ZeBrAS**: Default <br>
**Vorbedingung:** LSS00, LSS01, LSS02 <br>
**Härte:**<br>
"muss": [],<br>
"kann": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"hinweis": [] <br>
**Text:** Zu dieser verbeamteten Lehrkraft ist die Staatsangehörigkeit '" + LehrerStammdaten.staatsangehoerigkeitID + "' angegeben. Dabei handelt es sich jedoch nicht um eine Staatsangehörigkeit eines Mitgliedsstaats der Europäischen Union (EU) oder des Europäischen Wirtschaftsraums (EWR). Die vorgenommene Eintragung kann nur in Ausnahmefällen korrekt sein. Für Lehrkräfte, die neben einer ausländischen Staatsangehörigkeit auch die deutsche Staatsangehörigkeit besitzen, erfassen Sie bitte die Staatsangehörigkeit 'deutsch'. <br>
**Erläuterung:** - <br>
**Bedingung:** <br> LehrerStammdaten.rechtsverhaeltnis = L, N, P, W <br>
∧ LehrerStammdaten.staatsangehoerigkeitID ≠ DEU, BEL, BGR, DNK, EST, FIN, FRA, HRV, SVN, GRC, IRL, ISL, ITA, LVA, LIE, LTU, LUX, MLT, NLD, NOR, AUT, POL, PRT, ROU, SVK, SWE, CHE, ESP, CZE, HUN, GBR, CYP <br>

---
