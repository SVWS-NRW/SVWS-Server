package de.svws_nrw.core.exceptions;

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
	public DeveloperNotificationException(final @NotNull String pFehlermeldung) {
		super(pFehlermeldung);
	}

	/**
	 * Überprüft, ob eine Bedingung erfüllt ist und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param pBeschreibung Die Beschreibung der Bedingung.
	 * @param pErfuellt     Falls TRUE, wird eine DeveloperNotificationException geworfen.
	 */
	public static void check(final @NotNull String pBeschreibung, final boolean pErfuellt) {
		if (pErfuellt)
			throw new DeveloperNotificationException(pBeschreibung);
	}

  	/**
     * Überprüft, ob eine Bedingung erfüllt ist und wirft in diesem Fall eine DeveloperNotificationException.
     * Andernfalls wird der Parameter t zurückgegeben.
	 *
	 * @param pFehlermeldung Die Beschreibung der Bedingung.
  	 * @param pErgebnis Der Rückgabewert, falls es keinen Fehler gibt.
  	 * @param <T> Der Typ von pErgebnis.
  	 * @return Liefert pErgebnis, falls es keinen Fehler gibt.
	 */
	public static <@NotNull T> @NotNull T checkNull(final @NotNull String pFehlermeldung, final T pErgebnis) {
		if (pErgebnis == null)
			throw new DeveloperNotificationException(pFehlermeldung);
		return pErgebnis;
	}

}
