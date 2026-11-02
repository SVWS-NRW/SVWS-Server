package de.svws_nrw.repo.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Dieser Record enthält aggregierte Daten zu einem Jahrgang der gymnasialen Oberstufe in einem Schuljahresabschnitt.
 *
 * @param idJahrgang                die ID des Jahrgangs
 * @param kuerzelASDJahrgang        das Statistik-Kürzel des Jahrgangs
 * @param idSchuljahresabschnitt    die ID des Schuljahresabschnittes
 * @param anzahlKurse               die Anzahl der Oberstufen-Kurse, welche für den Jahrgang in dem Schuljahresabschnitt bereits erstellt wurden
 * @param anzahlNoten               die Anzahl der Leistungsdaten, welche für den Jahrgang in dem Schuljahresabschnitt bereits mit Noten versehen wurden
 */
public record GostJahrgangsinformation(long idJahrgang, @NotNull String kuerzelASDJahrgang, long idSchuljahresabschnitt, long anzahlKurse, long anzahlNoten) {

}
