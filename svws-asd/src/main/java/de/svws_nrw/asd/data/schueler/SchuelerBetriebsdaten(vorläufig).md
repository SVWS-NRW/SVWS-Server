# SchuelerBetriebsdaten

| Attribut              | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal  | Kommentar                                                          |
|-----------------------|----------|-------|----------|----------|--------------|--------------------------------------------------------------------|
| id                    | long     |       | false    |          | @ASD         | die ID des Datensatzes                                             |
| schueler_id           | long     |       | false    |          | @ASD         | die ID des Schülers                                                |
| betrieb_id            | long     |       | false    |          | ? Nachfragen | AdressID des Betriebeeintrags beim Schüler                         |
| beschaeftigungsart_id | Long     |       | true     |          | ? Nachfragen | ID der Beschäftigungsart des Schülers                              |
| vertragsbeginn        | String   |       | true     |          | -            | Datum Vertragsbeginn des Betriebeeintrags beim Schüler             |
| vertragsende          | String   |       | true     |          | -            | Datum des Vertragsende des Betriebeeintrags beim Schüler           |
| ausbilder             | String   |       | true     |          | -            | Ausbildername des Betriebeeintrags beim Schüler                    |
| allgadranschreiben    | Boolean  |       | false    |          | -            | Betrieb erhält Anschreiben Ja/Nein                                 |
| praktikum             | Boolean  |       | false    |          | -            | Gibt an ob es ein Praktikum ist beim Betriebeeintrags beim Schüler |
| sortierung            | Integer  |       | true     |          | -            | Sortierung des Betriebeeintrags beim Schüler                       |
| ansprechpartner_id    | Long     |       | true     |          | -            | AnsprechpartnerID des Betriebeeintrags beim Schüler                |
| betreuungslehrer_id   | Long     |       | true     |          | -            | BetreuungslehrerID des Betriebeeintrags beim Schüler               |
