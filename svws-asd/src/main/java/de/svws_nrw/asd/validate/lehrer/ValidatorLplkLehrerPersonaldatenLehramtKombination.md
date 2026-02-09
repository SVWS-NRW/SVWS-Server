### ValidatorLplkLehrerPersonaldatenLehramtKombination

**Zweig:** Lehrer-Personaldaten-Lehramt <br>
**DTOs:** LehrerPersonaldaten <br>
**Ausführungsbereich-UI:** Lehrkräfte, Reiter 'Personaldaten' <br>
**Anzeigebereich-UI:** Lehrämter (an der betroffenen Zeile) <br>
**Default-SVWS/ZeBrAS:** <br>
"svws": true, <br>
"zebras": true, <br>
**Default-Fehlerhärte:**<br>
"muss": [], <br>
"kann": ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"],<br>
"hinweis": []

---

**Fehlerkürzel:** LPLK0 <br>
**Altes-Fehlerkürzel:** AD413 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Neben dem Lehramtseintrag 'Schulverwaltungsassistenten/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_70" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_70" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK1 <br>
**Altes-Fehlerkürzel:** AD414 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Neben dem Lehramtseintrag 'Werkstattlehrern/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_52" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_52" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK2 <br>
**Altes-Fehlerkürzel:** AD415 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Die Lehramtseinträge 'Schulkindergärtner/-in', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)', 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)', 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht in Kombination mit anderen als diesen Lehramtseinträgen vorliegen. Bitte korrigieren Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>
[LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_56" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_57", "ID_58", "ID_60", "ID_61"]  <br>
∨ [LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_57" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_56", "ID_58", "ID_60", "ID_61"]  <br>
∨ [LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_58" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_56", "ID_57", "ID_60", "ID_61"]  <br>
∨ [LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_60" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_56", "ID_57", "ID_58", "ID_61"]  <br>
∨ [LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_61" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_56", "ID_57", "ID_58", "ID_60"]  <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK3 <br>
**Altes-Fehlerkürzel:** AD416 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Neben dem Lehramtseintrag 'Lehramtsanwärter/-in / Studienreferendar/-in' dürfen keine weiteren Lehramtseinträge vorliegen. Bitte überprüfen Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_98" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_98" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK4 <br>
**Altes-Fehlerkürzel:** AD417 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Die Lehramtseinträge 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Erzieher/-in mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Erzieher/-in ohne sonderpädagogische Zusatzausbildung'. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_58" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) = "ID_61" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK5 <br>
**Altes-Fehlerkürzel:** AD418 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Die Lehramtseinträge 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung' und 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung'. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_59" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) = "ID_62" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK6 <br>
**Altes-Fehlerkürzel:** AD419 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Die Lehramtseinträge 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)'. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_57" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) = "ID_60" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK7 <br>
**Altes-Fehlerkürzel:** AD420 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Neben dem Lehramtseintrag 'Handwerksmeistern/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_64" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_64" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK8 <br>
**Altes-Fehlerkürzel:** AD423 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Neben dem Lehramtseintrag 'Heilpädagogen/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_63" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_63" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK9 <br>
**Altes-Fehlerkürzel:** AD424 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Neben dem Lehramtseintrag 'Alltagshelfern/-innen' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_65" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_65" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

**Fehlerkürzel:** LPLK20 <br>
**Altes-Fehlerkürzel:** AD426 <br>
**SVWS/ZeBrAS**: Default <br>
**Fehlerhärte:** Default <br> 
**Fehlertext:** Neben dem Lehramtseintrag 'Studierende' sollten keine weiteren Lehramtseinträge vorliegen. Bitte korrigieren Sie Ihre Angaben. <br>
**Erläuterung:** - <br>
**Bedingung:** <br>LehrerPersonaldaten.lehraemter[].bezeichner(i) ∈ "ID_90" ∧ LehrerPersonaldaten.lehraemter[].bezeichner(j) ≠ "ID_90" <br>
i = beliebiger Lehramtseintrag einer Lehrkraft; j = beliebiger Lehramtseintrag einer Lehrkraft; i ≠ j <br>

---

