# Sprachpruefung

| Attribut                                    | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal | Kommentar                                                                                                |
|---------------------------------------------|----------|-------|----------|----------|-------------|----------------------------------------------------------------------------------------------------------|
| sprache                                     | String   |       | true     |          | @ASD        | Das einstellige Sprachkürzel des geprüften Faches                                                        |
| jahrgang                                    | String   |       | true     |          | @ASD        | Gibt an, in welchem ASD-Jahrgang die Prüfung abgelegt wurde                                              |
| anspruchsniveauId                           | Integer  |       | true     |          | @ASD        | ID der Bezeichnung des am Schulabschluss orientierten Anspruchsniveau der Sprachprüfung                  |
| pruefungsdatum                              | String   |       | true     |          | -           | Gibt das Datum an, an dem die Prüfung abgelegt wurde                                                     |
| ersetzteSprache                             | String   |       | true     |          | @ASD        | Sprache, die durch die Prüfung ersetzt wird                                                              |
| istHSUPruefung                              | boolean  |       | false    |          | @ASD        | Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht                                             |
| istFeststellungspruefung                    | boolean  |       | false    |          | @ASD        | Prüfung ist eine Sprachfeststellungsprüfung                                                              |
| kannErstePflichtfremdspracheErsetzen        | boolean  |       | false    |          | @ASD        | Durch die Prüfung kann die erste Pflichtfremdsprache ersetzt werden                                      |
| kannZweitePflichtfremdspracheErsetzen       | boolean  |       | false    |          | @ASD        | Durch die Prüfung kann die zweite Pflichtfremdsprache ersetzt werden                                     |
| kannWahlpflichtfremdspracheErsetzen         | boolean  |       | false    |          | @ASD        | Durch die Prüfung kann die Wahlpflichtfremdsprache ersetzt werden                                        |
| kannBelegungAlsFortgefuehrteSpracheErlauben | boolean  |       | false    |          | -           | Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden               |
| referenzniveau                              | String   |       | true     |          | @ASD        | Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde                             |
| note                                        | Integer  |       | true     |          | -           | Die Note, die in der Sprachprüfung erreicht wurde (1,2,3,4,5,6 oder null, wenn keine Note angegeben ist) |
