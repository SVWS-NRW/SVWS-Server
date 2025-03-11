# LehrerPersonalabschnittsdaten
| Attribut               | Datentyp | Länge | Nullable | CoreType | ASD-Merkmal | Kommentar                                                                                                              |
|------------------------|----------|-------|----------|----------|-------------|------------------------------------------------------------------------------------------------------------------------|
| id                     | long     |       | false    |          | @ASD        | Die ID des des Abschnitts für den Lehrers in der Datenbank.                                                            |
| idlehrer               | long     |       | false    |          | @ASD        | Die ID des Lehrers.                                                                                                    |
| idschuljahresabschnitt | long     |       | false    |          | @ASD        | Die ID des Schuljahresabschnitts, zu dem diese Abschnittsdaten gehören.                                                |
| pflichtstundensoll     | Double   |       | true     |          | @ASD        | Das Pflichtstundensoll des Lehrers.                                                                                    |
| rechtsverhaeltnis      | String   |       | true     |          | @ASD        | Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog. |
| beschaeftigungsart     | String   |       | true     |          | @ASD        | Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog.                                        |
| einsatzstatus          | String   |       | true     |          | @ASD        | Der Einsatzstatus (z.B. Stammschule, nur hier tätig)                                                                   |
| stammschulnummer       | String   |       | true     |          | -           | Die Schulnummer der Stammschule, sofern diese abweicht.                                                                |
