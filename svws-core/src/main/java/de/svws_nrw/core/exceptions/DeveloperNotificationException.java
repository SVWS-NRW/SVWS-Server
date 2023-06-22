package de.svws_nrw.core.exceptions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
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
			throw new DeveloperNotificationException(pVariablenname + " hat eine ungültige ID=" + pID + "!");
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
	 * Überprüft, ob eine Variable einen Wert hat.
	 *
	 * @param pVariablenname Der Name der Variablen.
	 * @param pVariable      Die Variable, die geprüft wird.
	 * @param pWert          Der Wert der Variablen.
	 *
	 * @throws DeveloperNotificationException falls pVariable gleich pWert ist..
	 */
	public static void ifEqual(final @NotNull String pVariablenname, final int pVariable, final int pWert) throws DeveloperNotificationException {
		if (pVariable == pWert)
			throw new DeveloperNotificationException(pVariablenname + "(" + pWert + ")" + " darf nicht gleich " + pWert + " sein!");
	}

	/**
	 * Überprüft, ob ein String leer ist und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param nameDerVariablen Der Name der Variablen.
	 * @param zeichenkette     Der String, welcher nicht "blank" sein darf.
	 *
	 * @throws DeveloperNotificationException falls der übergebene String leer ist.
	 */
	public static void ifStringIsBlank(final @NotNull String nameDerVariablen, final @NotNull String zeichenkette) {
		if (zeichenkette.isBlank())
			throw new DeveloperNotificationException(nameDerVariablen + " darf nicht 'blank' sein!");
	}

	/**
	 * Überprüft, ob ein Schlüssel bereits in einer Map existiert.
	 *
	 * @param <K> Der Schlüssel-Typ der Map.
	 * @param <V> Der zum Schlüssel zugeordnete Typ der Map.
	 * @param pMapName Der Name der Map.
	 * @param pMap     Die Map.
	 * @param pKey     Der Schlüssel der überprüft wird.
	 *
	 * @throws DeveloperNotificationException falls der Schlüssel in der Map bereits existiert.
	 */
	public static <@NotNull K, @NotNull V> void ifMapContains(final @NotNull String pMapName, @NotNull final Map<@NotNull K, @NotNull V> pMap, final @NotNull K pKey) throws DeveloperNotificationException {
		if (pMap.containsKey(pKey))
			throw new DeveloperNotificationException(pMapName + " hat bereits den KEY " + pKey + "");
	}

	/**
	 * Überprüft, ob ein Schlüssel nicht in einer Map existiert.
	 *
	 * @param <K> Der Schlüssel-Typ der Map.
	 * @param <V> Der zum Schlüssel zugeordnete Typ der Map.
	 * @param pMapName Der Name der Map.
	 * @param pMap     Die Map.
	 * @param pKey     Der Schlüssel der überprüft wird.
	 *
	 * @throws DeveloperNotificationException falls der Schlüssel nicht in der Map bereits existiert.
	 */
	public static <@NotNull K, @NotNull V> void ifMapNotContains(final @NotNull String pMapName, @NotNull final Map<@NotNull K, @NotNull V> pMap, final @NotNull K pKey) throws DeveloperNotificationException {
		if (!pMap.containsKey(pKey))
			throw new DeveloperNotificationException(pMapName + " hat nicht den KEY " + pKey + "");
	}

	/**
	 * Fügt das Mapping K --> V der Map hinzu. <br>
	 * Wirft eine DeveloperNotificationException, falls dem Schlüssel K bereits etwas zugeordnet ist.
	 *
	 * @param <K> Der Schlüssel-Typ des Mappings K --> V.
	 * @param <V> Der Schlüssel-Wert des Mappings K --> V.
	 * @param map   Die Map.
	 * @param key   Der Schlüssel des Mappings K --> V.
	 * @param value Der Wert des Mappings K --> V.
	 *
	 * @throws DeveloperNotificationException falls dem Schlüssel K bereits etwas zugeordnet ist.
	 */
	public static <@NotNull K, @NotNull V>  void ifMapPutOverwrites(final @NotNull Map<@NotNull K, @NotNull V> map, final @NotNull K key, final @NotNull V value) throws DeveloperNotificationException {
		if (map.containsKey(key))
			throw new DeveloperNotificationException("PUT von " + key + " --> " + value + " fehlgeschlagen, da bereits " + map.get(key) + " zugeordnet ist!");
		map.put(key, value);
	}

	/**
	 * Fügt das Mapping (K1, K2) --> V der Map hinzu. <br>
	 * Wirft eine DeveloperNotificationException, falls dem Schlüssel K bereits etwas zugeordnet ist.
	 *
	 * @param <K1>  Der Typ des 1. Schlüssels.
	 * @param <K2>  Der Typ des 2. Schlüssels.
	 * @param <V>   Der Typ des zugeordneten Wertes.
	 * @param map   Die Map.
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param value Der zugeordnete Wert.
	 *
	 * @throws DeveloperNotificationException falls dem Schlüssel K bereits etwas zugeordnet ist.
	 */
	public static <@NotNull K1, @NotNull K2, @NotNull V>  void ifMap2DPutOverwrites(final @NotNull HashMap2D<@NotNull K1, @NotNull K2, @NotNull V> map, final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull V value) throws DeveloperNotificationException {
		if (map.contains(key1, key2))
			throw new DeveloperNotificationException("PUT von (" + key1 + ", " + key2 + ") --> " + value + " fehlgeschlagen, da bereits " + map.getOrException(key1, key2) + " zugeordnet ist!");
		map.put(key1, key2, value);
	}

	/**
	 * Liefert den zugeordneten (nicht NULL) Wert des übergebenen Schlüssels.
	 * Wirft eine DeveloperNotificationException, falls dem Schlüssel K nichts oder NULL zugeordnet ist.
	 *
	 * @param <K> Der Schlüssel-Typ des Mappings K --> V.
	 * @param <V> Der Schlüssel-Wert des Mappings K --> V.
	 * @param map Die Map.
	 * @param key Der Schlüssel des Mappings K --> V.
	 *
	 * @return den zugeordneten (nicht NULL) Wert des übergebenen Schlüssels.
	 * @throws DeveloperNotificationException falls dem Schlüssel K nichts oder NULL zugeordnet ist.
	 */
	public static <@NotNull K, @NotNull V>  @NotNull V ifMapGetIsNull(@NotNull final Map<@NotNull K, @NotNull V> map, final @NotNull K key) throws DeveloperNotificationException {
		if (!map.containsKey(key))
			throw new DeveloperNotificationException("GET von " + key + " fehlgeschlagen, da kein Mapping existiert!");
		final V value = map.get(key);
		if (value == null)
			throw new DeveloperNotificationException("GET von " + key + " fehlgeschlagen, da es auf NULL mapped!");
		return value;
	}

	/**
	 * Liefert den zugeordneten (nicht NULL) Wert des übergebenen Schlüssels.
	 * Wirft eine DeveloperNotificationException, falls dem Schlüssel K nichts oder NULL zugeordnet ist.
	 *
	 * @param <K1>  Der Typ des 1. Schlüssels.
	 * @param <K2>  Der Typ des 2. Schlüssels.
	 * @param <V>   Der Typ des zugeordneten Wertes.
	 * @param map   Die Map.
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 *
	 * @return den zugeordneten (nicht NULL) Wert des übergebenen Schlüssels.
	 * @throws DeveloperNotificationException falls dem Schlüssel (K1, K2) nichts oder NULL zugeordnet ist.
	 */
	public static <@NotNull K1, @NotNull K2, @NotNull V>  @NotNull V ifMap2DGetIsNull(final @NotNull HashMap2D<@NotNull K1, @NotNull K2, @NotNull V> map, final @NotNull K1 key1, final @NotNull K2 key2) throws DeveloperNotificationException {
		if (!map.contains(key1, key2))
			throw new DeveloperNotificationException("GET von (" + key1 + ", " + key2 + ") fehlgeschlagen, da kein Mapping existiert!");
		return map.getNonNullOrException(key1, key2);
	}

	/**
	 * Liefert den zugeordneten (nicht NULL) Wert des übergebenen Schlüssels.
	 * Wirft eine DeveloperNotificationException, falls dem Schlüssel (K1, K2, K3) nichts oder NULL zugeordnet ist.
	 *
	 * @param <K1>  Der Typ des 1. Schlüssels.
	 * @param <K2>  Der Typ des 2. Schlüssels.
	 * @param <K3>  Der Typ des 3. Schlüssels.
	 * @param <V>   Der Typ des zugeordneten Wertes.
	 * @param map   Die Map.
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 *
	 * @return den zugeordneten (nicht NULL) Wert des übergebenen Schlüssels.
	 * @throws DeveloperNotificationException falls dem Schlüssel (K1, K2, K3) nichts oder NULL zugeordnet ist.
	 */
	public static <@NotNull K1, @NotNull K2, @NotNull K3, @NotNull V>  @NotNull V ifMap3DGetIsNull(final @NotNull HashMap3D<@NotNull K1, @NotNull K2, @NotNull K3, @NotNull V> map, final @NotNull K1 key1, final @NotNull K2 key2, final @NotNull K3 key3) throws DeveloperNotificationException {
		if (!map.contains(key1, key2, key3))
			throw new DeveloperNotificationException("GET von (" + key1 + ", " + key2 + ", " + key3 + ") fehlgeschlagen, da kein Mapping existiert!");
		return map.getNonNullOrException(key1, key2, key3);
	}

	/**
	 * Fügt ein Element der Liste hinzu, außer es erzeugt ein Duplikat, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <E>      Der Typ der Elemente der Liste
	 * @param listName Der Name der Liste.
	 * @param list     Die Liste.
	 * @param value    Das Element, welches hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException falls das Element bereits in der Liste existiert.
	 */
	public static <@NotNull E> void ifListAddsDuplicate(final @NotNull String listName, @NotNull final List<@NotNull E> list, final @NotNull E value) throws DeveloperNotificationException {
		if (list.contains(value))
			throw new DeveloperNotificationException(listName + " hat bereits das Element " + value + "!");
		list.add(value);
	}

	/**
	 * Entfernt ein Element aus der Liste, außer es existiert nicht, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <E>      Der Typ der Elemente der Liste
	 * @param listName Der Name der Liste.
	 * @param list     Die Liste.
	 * @param value    Das Element, welches entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException falls das Element nicht in der Liste existiert.
	 */
	public static <@NotNull E> void ifListRemoveFailes(final @NotNull String listName, @NotNull final List<@NotNull E> list, final @NotNull E value) throws DeveloperNotificationException {
		if (!list.remove(value))
			throw new DeveloperNotificationException(listName + " konnte Element " + value + " nicht entfernen!");
	}

	/**
	 * Liefert das erste NICHT-NULL Element der Liste.
	 *
	 * @param <E>      Der Typ der Elemente der Liste
	 * @param listName Der Name der Liste.
	 * @param list     Die Liste.
	 *
	 * @return das erste NICHT-NULL Element der Liste.
	 *
	 * @throws DeveloperNotificationException falls kein erstes NICHT-NULL Element existiert.
	 */
	public static <@NotNull E> @NotNull E ifListGetFirstFailes(final @NotNull String listName, @NotNull final List<@NotNull E> list) throws DeveloperNotificationException {
		if (list.isEmpty())
			throw new DeveloperNotificationException(listName + " hat kein erstes Element!");
		final E first = list.get(0);
		if (first == null)
			throw new DeveloperNotificationException(listName + " hat zwar ein erstes Element, aber es ist NULL!");
		return first;
	}

	/**
	 * Wirft eine Exception, falls die übergebene Liste leer ist.
	 *
	 * @param <E>         Der Typ der Elemente der Liste.
	 * @param listName    Der Name der Liste.
	 * @param collection  Die Liste.
	 *
	 * @throws DeveloperNotificationException falls das Array leer ist.
	 */
	public static <@NotNull E> void ifCollectionIsEmpty(final @NotNull String listName, final @NotNull Collection<@NotNull E> collection) throws DeveloperNotificationException {
		if (collection.isEmpty())
			throw new DeveloperNotificationException("Die Liste '" + listName + "' darf nicht leer sein!");
	}

	/**
	 * Fügt ein Element dem Set hinzu, außer es erzeugt ein Duplikat, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <E>      Der Typ der Elemente des Sets
	 * @param setName  Der Name des Sets.
	 * @param set      Das Set.
	 * @param value    Das Element, welches hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException falls das Element bereits im Set existiert.
	 */
	public static <@NotNull E> void ifSetAddsDuplicate(final @NotNull String setName, @NotNull final Set<@NotNull E> set, final @NotNull E value) throws DeveloperNotificationException {
		if (!set.add(value))
			throw new DeveloperNotificationException(setName + " hat bereits das Element " + value + "!");
	}

	/**
	 * Entfernt ein Element aus dem Set, außer es existiert nicht, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <E>      Der Typ der Elemente des Sets
	 * @param setName  Der Name des Sets.
	 * @param set      Das Set.
	 * @param value    Das Element, welches entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException falls das Element nicht im Set existiert.
	 */
	public static <@NotNull E> void ifSetRemoveFailes(final @NotNull String setName, @NotNull final Set<@NotNull E> set, final @NotNull E value) throws DeveloperNotificationException {
		if (!set.remove(value))
			throw new DeveloperNotificationException(setName + " konnte Element " + value + " nicht entfernen!");
	}

	/**
	 * Überprüft, ob ein Set ein Element bereits enthält, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <E>      Der Typ der Elemente des Sets
	 * @param setName  Der Name des Sets.
	 * @param set      Das Set.
	 * @param value    Das Element, welches nicht vorkommen darf.
	 *
	 * @throws DeveloperNotificationException falls das Element bereits im Set existiert.
	 */
	public static <@NotNull E>  void ifSetContains(final @NotNull String setName, @NotNull final Set<@NotNull E> set, final @NotNull E value) throws DeveloperNotificationException {
		if (set.contains(value))
			throw new DeveloperNotificationException(setName + " hat darf " + value + " nicht enthalten!");
	}

	/**
	 * Überprüft, ob ein Set ein Element nicht enthält, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <E>      Der Typ der Elemente des Sets
	 * @param setName  Der Name des Sets.
	 * @param set      Das Set.
	 * @param value    Das Element, welches vorkommen muss.
	 *
	 * @throws DeveloperNotificationException falls das Element nicht im Set existiert.
	 */
	public static <@NotNull E>  void ifSetNotContains(final @NotNull String setName, @NotNull final Set<@NotNull E> set, final @NotNull E value) throws DeveloperNotificationException {
		if (!set.contains(value))
			throw new DeveloperNotificationException(setName + " muss " + value + " enthalten!");
	}

	/**
	 * Liefert die Zahl des umgewandelten Strings.
	 *
	 * @param s Der String der in ein int umgewandelt werden soll.
	 *
	 * @return die Zahl des umgewandelten Strings.
	 */
	public static int ifNotInt(final String s) {
		if (s == null)
			throw new DeveloperNotificationException("NULL-String kann nicht in eine Zahl umgwandelt werden");
		return Integer.parseInt(s);
	}

	/**
	 * Wirft eine Exception, falls das übergebene Array leer ist.
	 *
	 * @param <E>        Der Typ der Elemente des Arrays.
	 * @param arrayName  Der Name des Arrays.
	 * @param values     Das Array.
	 *
	 * @throws DeveloperNotificationException falls das Array leer ist.
	 */
	public static <@NotNull E> void ifArrayIsEmpty(final @NotNull String arrayName, final @NotNull E[] values) throws DeveloperNotificationException {
		if (values.length == 0)
			throw new DeveloperNotificationException("Das Array '" + arrayName + "' darf nicht leer sein!");
	}


}
