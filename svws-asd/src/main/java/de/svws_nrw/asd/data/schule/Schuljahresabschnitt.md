# Schuljahresabschnitt

| Attribut           | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal | Kommentar                                     |
|--------------------|----------|-------|----------|----------|-------------|-----------------------------------------------|
| id                 | long     |       | False    |          |             | Die ID des Schuljahresabschnittes             |
| schuljahr          | int      |       | False    |          |             | Das Schuljahr, in welchem der Abschnitt liegt |
| abschnitt          | int      |       | False    |          |             | Die Nummer des Abschnitts im Schuljahr        |
| idVorigerAbschnitt | Long     |       | True     |          |             | die ID des vorigen Schuljahresabschnittes     |
| idFolgeAbschnitt   | Long     |       | True     |          |             | die ID des folgenden Schuljahresabschnittes   |
