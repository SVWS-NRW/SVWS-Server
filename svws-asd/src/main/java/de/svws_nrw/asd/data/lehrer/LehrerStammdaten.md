# LehrerStammdaten
| Attribut               | Datentyp | Länge | Nullable | CoreType                               | ASD-Merkmal | Kommentar                                                    |
|------------------------|----------|-------|----------|----------------------------------------|-------------|--------------------------------------------------------------|
| id                     | long     |       | false    | \-                                     | @ASD        | Die ID des Lehrers                                           |
| kuerzel                | String   | 10    | false    | \-                                     | @ASD        | Das Kürzel des Lehrers                                       |
| personaltyp            | String   | 20    | false    | [PersonalTyp](../types/PersonalTyp.md) | ?           | Die Bezeichnung des Personals-Typs des Lehrers               |
| anrede                 | String   | 10    | true     | \-                                     | \-          | Ggf. die Anrede des Lehrers                                  |
| titel                  | String   | 20    | true     | \-                                     | @ASD        | Ggf. ein akademischer Grad des Lehrers                       |
| amtsbezeichnung        | String   | 15    | true     | \-                                     | \-          | Ggf. die Amtsbezeichnung des Lehrers                         |
| nachname               | String   | 120   | false    | \-                                     | @ASD        | Der Nachname des Lehrers                                     |
| vorname                | String   | 80    | false    | \-                                     | @ASD        | Der Vorname des Lehrers                                      |
| geschlecht             | int      |       | false    | [Geschlecht](../types/Geschlecht.md)   | @ASD        | Die ID des Geschlechtes                                      |
| geburtsdatum           | String   |       | true     | \-                                     | @ASD        | Das Geburtsdatum des Lehrers - ISO 8601 codiert              |
| staatsangehoerigkeitID | String   | 3     | true     | \-                                     | @ASD        | Ggf. die ID für die Staatsangehörigkeit des Lehrers          |
| strassenname           | String   | 55    | true     | \-                                     | \-          | Ggf. der Straßenname im Wohnort des Lehrers                  |
| hausnummer             | String   | 10    | true     | \-                                     | \-          | Ggf. die Hausnummer zur Straße im Wohnort des Lehrers        |
| hausnummerZusatz       | String   | 30    | true     | \-                                     | \-          | Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers  |
| wohnortID              | Long     |       | true     | \-                                     | \-          | Ggf. die ID des Wohnortes des Lehrers                        |
| ortsteilID             | Long     |       | true     | \-                                     | \-          | Ggf. die ID des Ortsteils im Wohnort des Lehrers             |
| telefon                | String   | 20    | true     | \-                                     | \-          | Ggf. die Telefonnummer des Lehrers                           |
| telefonMobil           | String   | 20    | true     | \-                                     | \-          | Ggf. die Mobilnummer des Lehrers                             |
| emailPrivat            | String   | 100   | true     | \-                                     | \-          | Ggf. die private Email-Adresse des Lehrers                   |
| emailDienstlich        | String   | 100   | true     | \-                                     | \-          | Ggf. die dienstliche Email-Adresse des Lehrers               |
| foto                   | String   | \-    | true     | \-                                     | \-          | Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.) |

-[] Hier sind noch 3 neue Zeilen dazu gekommen, die eingepflegt und markiert werden müssen

## Java-Code

- [LehrerStammdaten.java](../../svws-asd-lib/src/main/java/de/svws_nrw/asd/data/lehrer/LehrerStammdaten.java)
- [Geschlecht.java](../../svws-asd-lib/src/main/java/de/svws_nrw/asd/types/Geschlecht.java)
- [PersonalTyp.java](../../svws-asd-lib/src/main/java/de/svws_nrw/asd/types/PersonalTyp.java)
 
