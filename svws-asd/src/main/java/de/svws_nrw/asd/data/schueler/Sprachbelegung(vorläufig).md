# Sprachbelegung

| Attribut             | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal | Kommentar                                                                                                   |
|----------------------|----------|-------|----------|----------|-------------|-------------------------------------------------------------------------------------------------------------|
| sprache              | String   |       | false    |          | @ASD        | Das einstellige Sprachkürzel des belegten Faches                                                            |
| reihenfolge          | Integer  |       | true     |          | @ASD        | Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde                                  |
| belegungVonJahrgang  | String   |       | true     |          | @ASD        | Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde                                                |
| belegungVonAbschnitt | Integer  |       | true     |          | @ASD        | Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde                            |
| belegungBisJahrgang  | String   |       | true     |          | @ASD        | Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde                |
| belegungBisAbschnitt | Integer  |       | true     |          | @ASD        | Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde |
| referenzniveau       | String   |       | true     |          | @ASD        | Das Referenzniveau, welches bisher erreicht wurde                                                           |
| hatKleinesLatinum    | boolean  |       | false    |          | -           | Gibt an, ob das kleine Latinum erreicht wurde oder nicht.                                                   |
| hatLatinum           | boolean  |       | false    |          | -           |  Gibt an, ob das Latinum erreicht wurde oder nicht.                                                         |
| hatGraecum           | boolean  |       | false    |          | -           | Gibt an, ob das Graecum erreicht wurde oder nicht.                                                          |
| hatHebraicum         | boolean  |       | false    |          | -           | Gibt an, ob das Hebraicum erreicht wurde oder nicht.                                                        |
|                      |          |       |          |          |             |                                                                                                             |
