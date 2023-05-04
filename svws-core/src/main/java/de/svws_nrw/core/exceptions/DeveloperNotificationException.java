package de.svws_nrw.core.exceptions;

import java.util.Map;

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
	public static void ifTrue(final @NotNull String pBeschreibung, final boolean pErfuellt) {
		if (pErfuellt)
			throw new DeveloperNotificationException(pBeschreibung);
	}

	/**
	 * Überprüft, ob eine Variable eine ungültige (negative) ID hat.
	 *
	 * @param pVariablenname Der Name der Variablen.
	 * @param pID Falls negativ, wird eine DeveloperNotificationException geworfen.
	 *
	 * @throws DeveloperNotificationException falls pID negativ.
	 */
	public static void ifInvalidID(final @NotNull String pVariablenname, final long pID) throws DeveloperNotificationException {
		if (pID < 0)
			throw new DeveloperNotificationException("Ungültige ID für " + pVariablenname + "(" + pID + ")");
	}

  	/**
     * Überprüft, ob ein pWert NULL ist und wirft in diesem Fall eine DeveloperNotificationException.
     * Andernfalls wird pWert selbst zurückgegeben.
	 *
	 * @param pBeschreibung Die Beschreibung was nicht NULL sein darf.
  	 * @param pWert Der Rückgabewert, falls es keinen Fehler gibt.
  	 * @param <T> Der Typ von pErgebnis.
  	 *
  	 * @return Liefert pWert, falls es nicht NULL ist.
	 * @throws DeveloperNotificationException falls pWert NULL ist.
	 */
	public static <@NotNull T> @NotNull T ifNull(final @NotNull String pBeschreibung, final T pWert) throws DeveloperNotificationException {
		if (pWert == null)
			throw new DeveloperNotificationException(pBeschreibung + " sollte nicht NULL sein!");
		return pWert;
	}

  	/**
	 * Überprüft, ob eine pWert kleiner ist als pMinimum und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param pVariablenname Der Name der Variablen.
	 * @param pWert          Der Wert der Variablen.
	 * @param pMinimum       Der kleinste noch erlaubte Wert.
	 *
	 * @throws DeveloperNotificationException falls pWert kleiner als pMinimum ist.
	 */
	public static void ifSmaller(final @NotNull String pVariablenname, final int pWert, final int pMinimum) throws DeveloperNotificationException {
		if (pWert < pMinimum)
			throw new DeveloperNotificationException(pVariablenname + "(" + pWert + ")" + " darf nicht kleiner sein als " + pMinimum + "!");
	}


	/**
	 * Überprüft, ob eine pWert größer ist als pMaximum und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param pVariablenname Der Name der Variablen.
	 * @param pWert          Der Wert der Variablen.
	 * @param pMaximum       Der größte noch erlaubte Wert.
	 *
	 * @throws DeveloperNotificationException falls pWert größer als pMaximum ist.
	 */
	public static void ifGreater(final @NotNull String pVariablenname, final int pWert, final int pMaximum) throws DeveloperNotificationException {
		if (pWert > pMaximum)
			throw new DeveloperNotificationException(pVariablenname + "(" + pWert + ")"  + " darf nicht größer sein als " + pMaximum + "!");
	}

	/**
	 * Überprüft, ob ein Schlüssel bereits in einer Map existiert.
	 * @param <K> Der Schlüssel-Typ der Map.
	 * @param <V> Der zum Schlüssel zugeordnete Typ der Map.
	 * @param pMapName Der Name der Map.
	 * @param pMap     Die Map.
	 * @param pKey     Der Schlüssel der überprüft wird.
	 *
	 * @throws DeveloperNotificationException falls der Schlüssel in der Map bereits existiert.
	 */
	public static <@NotNull K, @NotNull V> void ifDuplicate(final @NotNull String pMapName, @NotNull final Map<@NotNull K, @NotNull V> pMap, final @NotNull K pKey) throws DeveloperNotificationException {
		if (pMap.containsKey(pKey))
			throw new DeveloperNotificationException(pMapName + " hat bereits den KEY(" + pKey + ")");
	}

	/**
	 * Fügt das Mapping 'K pKey' --> 'V pValue' der Map hinzu.
	 * Wirft eine DeveloperNotificationException, falls dem Schlüssel K bereits etwas zugeordnet ist.
	 *
	 * @param <K> Der Schlüssel-Typ des Mappings K --> V.
	 * @param <V> Der Schlüssel-Wert des Mappings K --> V.
	 * @param pMap   Die Map.
	 * @param pKey   Der Schlüssel des Mappings K --> V.
	 * @param pValue Der Wert des Mappings K --> V.
	 *
	 * @throws DeveloperNotificationException falls dem Schlüssel K bereits etwas zugeordnet ist.
	 */
	public static <@NotNull K, @NotNull V>  void ifMapPutOverwrites(@NotNull final Map<@NotNull K, @NotNull V> pMap, final @NotNull K pKey, final @NotNull V pValue) throws DeveloperNotificationException {
		if (pMap.containsKey(pKey))
			throw new DeveloperNotificationException("Hinzufügen von " + pKey + " --> " + pValue + " fehlgeschlagen, da " + pKey + " --> " + pMap.get(pKey) + " existiert!");
		pMap.put(pKey, pValue);
	}

}
