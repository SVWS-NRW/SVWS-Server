package de.svws_nrw.core.utils;

import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für DTO. Primär um die fehlenden Konstruktoren zu ersetzen.
 *
 * @author Benjamin A. Bartsch
 */
public final class DTOUtils {

	private DTOUtils() {
	}

    /**
     * Liefert ein neues {@link GostBlockungsergebnisKursSchuelerZuordnung}-Objekt.
     *
     * @param idKurs      Die Datenbank-ID des Kurses.
     * @param idSchueler  Die Datenbank-ID des Schülers.
     *
     * @return ein neues {@link GostBlockungsergebnisKursSchuelerZuordnung}-Objekt.
     *
     */
    public static @NotNull GostBlockungsergebnisKursSchuelerZuordnung newGostBlockungsergebnisKursSchuelerZuordnung(final long idKurs, final long idSchueler) {
        final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z = new GostBlockungsergebnisKursSchuelerZuordnung();
        z.idKurs = idKurs;
        z.idSchueler = idSchueler;
        return z;
    }

}
