package de.nrw.schule.svws.core.kursblockung;

import jakarta.validation.constraints.NotNull;

/** Erzeugt ein Fehler-Exception, die während der Schülerblockung entstehen kann.
 * 
 * @author Benjamin A. Bartsch */
public class SchuelerblockungException extends RuntimeException {

	private static final long serialVersionUID = -7308290111735862056L;

	/** Erzeugt ein Fehler-Exception, die während der Kursblockung entstehen kann.
	 * 
	 * @param fehlermeldung Eine Beschreibung des Fehlers. */
	public SchuelerblockungException(@NotNull String fehlermeldung) {
		super(fehlermeldung);
	}

}
