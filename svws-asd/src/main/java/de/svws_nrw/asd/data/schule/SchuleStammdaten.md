# SchuleStammdaten

| Attribut                | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal | Kommentar                                                              |
|-------------------------|----------|-------|----------|----------|-------------|------------------------------------------------------------------------|
| schulNr                 | long     |       | False    |          | @asd        | die Schulnummer der Schule                                             |
| schulform               | String   |       | False    |          | @asd        | Das Kürzel der Schulform                                               |
| bezeichnung1            | String   |       | False    |          | @asd        | die Bezeichnung 1 der Schule                                           |
| bezeichnung2            | String   |       | True     |          | @asd        | die Bezeichnung 2 der Schule                                           |
| bezeichnung3            | String   |       | True     |          | @asd        | die Bezeichnung 3 der Schule                                           |
| strassenname            | String   |       | True     |          | @asd        | der Straßenname der Straße in der die Schule liegt.                    |
| hausnummer              | String   |       | True     |          | @asd        | Ggf. die Hausnummer zur Straße in der die Schule liegt.                |
| hausnummerZusatz        | String   |       | True     | ?        |             | Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.          |
| plz                     | String   |       | True     |          | @asd        | die Postleitzahl der Schule                                            |
| ort                     | String   |       | True     |          | @asd        | der Ort der Schule                                                     |
| telefon                 | String   |       | True     |          | @asd ?      | die Telefonnummer der Schule                                           |
| fax                     | String   |       | True     |          | @asd ?      | die Faxnummer der Schule                                               |
| email                   | String   |       | True     |          | @asd        | die Mailadresse der Schule                                             |
| webAdresse              | String   |       | True     |          | @asd        | die Adresse der Homepage der Schule                                    |
| idSchuljahresabschnitt  | long     |       | False    |          | ?           | die ID des Schuljahresabschnittes, in welchem sich die Schule befindet |
| anzJGS_Jahr             | long     |       | False    |          | -           | Anzahl der Abschnitte pro Jahrgangsstufe                               |
| dauerUnterrichtseinheit | long     |       | False    |          | @asd        | Dauer einer Unterrichtseinheit                                         |
