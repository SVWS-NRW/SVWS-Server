# SchuelerSchulbesuchsdaten

| Attribut                          | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal                 | Kommentar                                                                                     |
|-----------------------------------|----------|-------|----------|----------|-----------------------------|-----------------------------------------------------------------------------------------------|
| id                                | long     |       | false    |          | @ASD                        | die ID des Schülerdatensatzes                                                                 |
| vorigeSchulnummer                 | String   |       | true     |          | @ASD                        | die Schulnummer der vorher besuchten Schule                                                   |
| vorigeAllgHerkunft                | String   |       | true     |          | @ASD                        | die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule |
| vorigeEntlassdatum                | String   |       | true     |          | -                           | das Entlassdatum an der zuvor besuchten Schule                                                |
| vorigeEntlassjahrgang             | String   |       | true     |          | ? Nachfragen, vermutlich ja | der Entlassjahrgang an der zuvor besuchten Schule                                             |
| vorigeArtLetzteVersetzung         | String   |       | true     |          | ? Nachfragen, vermutlich ja | die Art der letzten Versetzung an der zuvor besuchten Schule                                  |
| vorigeBemerkung                   | String   |       | true     |          | -                           | die Art der letzten Versetzung an der zuvor besuchten Schule                                  |
| vorigeEntlassgrundID              | Long     |       | true     |          | -                           | die ID des Grundes für die Entlassung von der zuvor besuchten Schule"                         |
| vorigeAbschlussartID              | String   |       | true     |          | @ASD                        | die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde                  |
| entlassungDatum                   | String   |       | true     |          | @ASD                        | das Entlassdatum von dieser Schule                                                            |
| entlassungJahrgang                | String   |       | true     |          | @ASD                        | der Jahrgang bei der Entlassung von dieser Schule                                             |
| entlassungGrundID                 | Long     |       | true     |          | -                           | die ID des Grundes für die Entlassung von dieser Schule                                       |
| entlassungAbschlussartID          | String   |       | true     |          | ? Nachfragen                | die ID des Abschlusses, welcher an dieser Schule erworben wurde                               |
| aufnehmdendSchulnummer            | String   |       | true     |          | in UAG 2 in Klärung         | die Schulnummer der aufnehmenden Schule nach einer Entlassung                                 |
| aufnehmdendWechseldatum           | String   |       | true     |          | -                           | das Datum beim Wechsel zu einer aufnehmenden Schule                                           |
| aufnehmdendBestaetigt             | Boolean  |       | true     |          | -                           | gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat                                  |
| grundschuleEinschulungsjahr       | Integer  |       | true     |          | @ASD                        | das Jahr der Einschulung in die Grundschule                                                   |
| grundschuleEinschulungsartID      | Long     |       | true     |          | @ASD                        | die ID der Einschulungsart in die Grundschule                                                 |
| grundschuleJahreEingangsphase     | Integer  |       | true     |          | @ASD                        | die Anzahl der Jahre in der Schuleingangsphase der Grundschule                                |
| grundschuleUebergangsempfehlungID | Long     |       | true     |          | @ASD                        | die ID für die Übergangsempfehlung der Grundschule in die Sekundarstufe I                     |
| sekIWechsel                       | Integer  |       | true     |          | -                           | das Jahr des Wechsels in die Sekundarstufe I                                                  |
| sekIErsteSchulform                | String   |       | true     |          | -                           | das Kürzel der ersten Schulform in der Sekundarstufe I                                        |
| sekIIWechsel                      | Integer  |       | true     |          | -                           | das Jahr des Wechsels in die Sekundarstufe II                                                 |
|                                   |          |       |          |          |                             |                                                                                               |
|                                   |          |       |          |          |                             |                                                                                               |
