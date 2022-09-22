package de.nrw.schule.svws.core.klausurblockung;

import jakarta.validation.constraints.NotNull;

/** Erzeugt ein Fehler-Exception, die während der Klausurblockung entstehen kann.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungException extends RuntimeException {

	private static final long serialVersionUID = 6387350163760202698L;

	/** Erzeugt ein Fehler-Exception, die während der Kursblockung entstehen kann.
	 * 
	 * @param fehlermeldung Eine Beschreibung des Fehlers. */
	public KlausurblockungException(@NotNull String fehlermeldung) {
		super(fehlermeldung);
	}

}
