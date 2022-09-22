package de.nrw.schule.svws.core.kursblockung;

import jakarta.validation.constraints.NotNull;

/** Erzeugt ein Fehler-Exception, die während der Kursblockung entstehen kann.
 * 
 * @author Benjamin A. Bartsch */
public class KursblockungException extends RuntimeException {

	private static final long serialVersionUID = -8026873013947670692L;

	/** Erzeugt ein Fehler-Exception, die während der Kursblockung entstehen kann.
	 * 
	 * @param fehlermeldung Eine Beschreibung des Fehlers. */
	public KursblockungException(@NotNull String fehlermeldung) {
		super(fehlermeldung);
	}

}
