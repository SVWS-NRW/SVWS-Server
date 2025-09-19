### [ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll.java ](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/klehr/asd-erste-pl-spezifikation-als-markdown-erstellen/svws-asd/src/main/java/de/svws_nrw/asd/validate/lehrer/ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll.md)

**Schulformen:** ["G", "H", "V", "S", "KS", "R", "PS", "SK", "GE", "FW", "HI", "WF", "GY", "WB", "BK", "SR", "SG", "SB"]<br>
**Fehlerhärte:** Kann <br>

---
**Fehlerkürzel:** LAP0 <br>
**Altes-Fehlerkürzel:** ? <br>
**Fehlertext:** Kein Wert im Feld 'pflichtstundensoll'. <br>
**Erläuterung:** Das Feld Pflichtstundensoll muss einen Wert aufweisen. <br>
**Bedingung:** [LehrerPersonalabschnittsdaten.pflichtstundensoll](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) = @ <br>

---
**Fehlerkürzel:** LAP1 <br>
**Altes-Fehlerkürzel:** AD51 ?<br>
**Fehlertext:** Unzulässiger Wert im Feld 'Pflichtstundensoll'. Zulässig sind im Stundenmodell Werte im Bereich von 0,00 bis 41,00 Wochenstunden. Im Minutenmodell zwischen 0,00 und 1845,00 Minuten. _(Text noch anzupassen)_ <br>
**Bedingung:** [LehrerPersonalabschnittsdaten.pflichtstundensoll](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) < 0 ∨ [LehrerPersonalabschnittsdaten.pflichtstundensoll](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) > 41.00 <br>

---
**Fehlerkürzel:** LAP2 <br>
**Altes-Fehlerkürzel:** AD51 ?<br>
**Fehlertext:** Bei Lehrkräften, die von einer anderen Schule abgeordnet wurden (Einsatzstatus = 'nicht Stammschule, aber auch hier tätig'), darf das Pflichtstundensoll nicht 0,00 betragen. <br>
**Bedingung:** [LehrerPersonalabschnittsdaten.Einsatzstatus](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) = "B" ∧ [LehrerPersonalabschnittsdaten.pflichtstundensoll](https://git.svws-nrw.de/svws/SVWS-Server/-/blob/dev/svws-asd/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerPersonalabschnittsdaten.java?ref_type=heads) = 0.00

---
