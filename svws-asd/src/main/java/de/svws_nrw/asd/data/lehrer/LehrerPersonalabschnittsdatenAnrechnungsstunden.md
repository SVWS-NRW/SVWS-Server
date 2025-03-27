# LehrerPersonalabschnittsdatenAnrechnungsstunden
| Attribut          | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal | Kommentar                                                            |
|-------------------|----------|-------|----------|----------|-------------|----------------------------------------------------------------------|
| id                | long     |       | false    |          | @ASD        | Die ID für den Eintrag von Anrechnungsstunden.                       |
| idabschnittsdaten | long     |       | false    |          | @ASD        | Die ID der Lehrerabschnittsdaten.                                    |
| idGrund           | long     |       | false    |          | @ASD        | Die ID des Anrechnungsgrundes.                                       |
| anzahl            | double   |       | false    |          | @ASD        | Die Anzahl der Anrechnungsstunden, welche dem Grund zugeordnet sind. |
