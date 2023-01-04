package de.nrw.schule.svws.core.exceptions;

import jakarta.validation.constraints.NotNull;

/** 
 * Mit dieser Klasse soll der User über einen Fehler informiert werden.
 * Eine Exception von diesem Typ soll nur dann verwendet werden, 
 * wenn die Fehlerursache vom Entwicklerteam ausgeht und dieser somit den Fehler <b>nicht beheben</b> kann.
 * 
 * @author Benjamin A. Bartsch 
 */
public class DeveloperNotificationException extends RuntimeException {

	private static final long serialVersionUID = 9106453927748350030L;

	/** 
	 * Erzeugt eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 * 
	 * @param pFehlermeldung Eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 */
	public DeveloperNotificationException(@NotNull String pFehlermeldung) {
		super(pFehlermeldung);
	}

}
