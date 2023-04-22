package de.svws_nrw.core.exceptions;

import jakarta.validation.constraints.NotNull;

/**
 * Mit dieser Klasse soll der User über einen Fehler informiert werden.
 * Eine Exception von diesem Typ soll nur dann verwendet werden,
 * wenn die Fehlerursache vom User ausgeht und dieser somit den Fehler <b>selbst beheben</b> kann.
 *
 * @author Benjamin A. Bartsch
 */
public class UserNotificationException extends RuntimeException {

	private static final long serialVersionUID = -6612891372229214720L;

	/**
	 * Erzeugt eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 *
	 * @param pFehlermeldung Eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 */
	public UserNotificationException(final @NotNull String pFehlermeldung) {
		super(pFehlermeldung);
	}

	/**
	 * Überprüft, ob eine Bedingung erfüllt ist und wirft in diesem Fall eine UserNotificationException.
	 *
	 * @param pBeschreibung Die Beschreibung der Bedingung.
	 * @param pErfuellt     Falls TRUE, wird eine UserNotificationException geworfen.
	 */
	public static void check(final @NotNull String pBeschreibung, final boolean pErfuellt) {
		if (pErfuellt)
			throw new UserNotificationException(pBeschreibung);
	}

}
