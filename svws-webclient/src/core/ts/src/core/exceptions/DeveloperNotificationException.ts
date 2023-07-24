import { HashMap2D } from '../../core/adt/map/HashMap2D';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { RuntimeException } from '../../java/lang/RuntimeException';
import type { JavaSet } from '../../java/util/JavaSet';
import type { Collection } from '../../java/util/Collection';
import type { List } from '../../java/util/List';
import { JavaString } from '../../java/lang/JavaString';
import type { JavaMap } from '../../java/util/JavaMap';
import { HashMap3D } from '../../core/adt/map/HashMap3D';

export class DeveloperNotificationException extends RuntimeException {

	private static readonly serialVersionUID : number = 9106453927748350030;


	/**
	 * Erzeugt eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 *
	 * @param pFehlermeldung Eine Beschreibung des Fehlers, die dem User hilft die Ursache des Fehlers zu beheben.
	 */
	public constructor(pFehlermeldung : string) {
		super(pFehlermeldung);
	}

	/**
	 * Überprüft, ob eine Bedingung erfüllt ist und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param pBeschreibung Die Beschreibung der Bedingung.
	 * @param pErfuellt     Falls TRUE, wird eine DeveloperNotificationException geworfen.
	 */
	public static ifTrue(pBeschreibung : string, pErfuellt : boolean) : void {
		if (pErfuellt)
			throw new DeveloperNotificationException(pBeschreibung)
	}

	/**
	 * Überprüft, ob eine Variable eine ungültige (negative) ID hat.
	 *
	 * @param pVariablenname Der Name der Variablen.
	 * @param pID Falls negativ, wird eine DeveloperNotificationException geworfen.
	 *
	 * @throws DeveloperNotificationException falls pID negativ.
	 */
	public static ifInvalidID(pVariablenname : string, pID : number) : void {
		if (pID < 0)
			throw new DeveloperNotificationException(pVariablenname! + " hat eine ungültige ID=" + pID + "!")
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
	public static ifNull<T>(pBeschreibung : string, pWert : T | null) : T {
		if (pWert === null)
			throw new DeveloperNotificationException(pBeschreibung! + " sollte nicht NULL sein!")
		return pWert;
	}

	/**
	 * Überprüft, ob ein pWert nicht in dem Bereich von min und max liegt und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param pVariablenname Der Name der Variablen.
	 * @param pWert          Der Wert der Variablen.
	 * @param pMinimum       Der kleinste noch erlaubte Wert.
	 * @param pMaximum       Der größte noch erlaubte Wert.
	 *
	 * @throws DeveloperNotificationException falls pWert kleiner als pMinimum ist.
	 */
	public static ifNotInRange(pVariablenname : string, pWert : number, pMinimum : number, pMaximum : number) : void {
		if ((pWert < pMinimum) || (pWert > pMaximum))
			throw new DeveloperNotificationException(pVariablenname! + "(" + pWert + ") muss in dem Bereich [ " + pMinimum + ", " + pMaximum + " ] liegen!")
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
	public static ifSmaller(pVariablenname : string, pWert : number, pMinimum : number) : void {
		if (pWert < pMinimum)
			throw new DeveloperNotificationException(pVariablenname! + "(" + pWert + ") darf nicht kleiner sein als " + pMinimum + "!")
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
	public static ifGreater(pVariablenname : string, pWert : number, pMaximum : number) : void {
		if (pWert > pMaximum)
			throw new DeveloperNotificationException(pVariablenname! + "(" + pWert + ") darf nicht größer sein als " + pMaximum + "!")
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
	public static ifEqual(pVariablenname : string, pVariable : number, pWert : number) : void {
		if (pVariable === pWert)
			throw new DeveloperNotificationException(pVariablenname! + "(" + pWert + ") darf nicht gleich " + pWert + " sein!")
	}

	/**
	 * Überprüft, ob ein String leer ist und wirft in diesem Fall eine DeveloperNotificationException.
	 *
	 * @param nameDerVariablen Der Name der Variablen.
	 * @param zeichenkette     Der String, welcher nicht "blank" sein darf.
	 *
	 * @throws DeveloperNotificationException falls der übergebene String leer ist.
	 */
	public static ifStringIsBlank(nameDerVariablen : string, zeichenkette : string) : void {
		if (JavaString.isBlank(zeichenkette))
			throw new DeveloperNotificationException(nameDerVariablen! + " darf nicht 'blank' sein!")
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
	public static ifMapContains<K, V>(pMapName : string, pMap : JavaMap<K, V>, pKey : K) : void {
		if (pMap.containsKey(pKey))
			throw new DeveloperNotificationException(pMapName! + " hat bereits den KEY " + pKey + "")
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
	public static ifMapNotContains<K, V>(pMapName : string, pMap : JavaMap<K, V>, pKey : K) : void {
		if (!pMap.containsKey(pKey))
			throw new DeveloperNotificationException(pMapName! + " hat nicht den KEY " + pKey + "")
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
	public static ifMapPutOverwrites<K, V>(map : JavaMap<K, V>, key : K, value : V) : void {
		if (map.containsKey(key))
			throw new DeveloperNotificationException("PUT von " + key + " --> " + value + " fehlgeschlagen, da bereits " + map.get(key) + " zugeordnet ist!")
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
	public static ifMap2DPutOverwrites<K1, K2, V>(map : HashMap2D<K1, K2, V>, key1 : K1, key2 : K2, value : V) : void {
		if (map.contains(key1, key2))
			throw new DeveloperNotificationException("PUT von (" + key1 + ", " + key2 + ") --> " + value + " fehlgeschlagen, da bereits " + map.getOrException(key1, key2) + " zugeordnet ist!")
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
	public static ifMapGetIsNull<K, V>(map : JavaMap<K, V>, key : K) : V {
		if (!map.containsKey(key))
			throw new DeveloperNotificationException("GET von " + key + " fehlgeschlagen, da kein Mapping existiert!")
		const value : V | null = map.get(key);
		if (value === null)
			throw new DeveloperNotificationException("GET von " + key + " fehlgeschlagen, da es auf NULL mapped!")
		return value;
	}

	/**
	 * Entfernt ein Mapping aus der Map, außer es existiert nicht, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <K> Der Schlüssel-Typ des Mappings K --> V.
	 * @param <V> Der Schlüssel-Wert des Mappings K --> V.
	 * @param map Die Map.
	 * @param key Der Schlüssel des Mappings K --> V.
	 *
	 * @throws DeveloperNotificationException falls das Element nicht in der Map existiert.
	 */
	public static ifMapRemoveFailes<K, V>(map : JavaMap<K, V>, key : K) : void {
		if (!map.containsKey(key))
			throw new DeveloperNotificationException("REMOVE von " + key + " fehlgeschlagen, da kein Mapping existiert!")
		map.remove(key);
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
	public static ifMap2DGetIsNull<K1, K2, V>(map : HashMap2D<K1, K2, V>, key1 : K1, key2 : K2) : V {
		if (!map.contains(key1, key2))
			throw new DeveloperNotificationException("GET von (" + key1 + ", " + key2 + ") fehlgeschlagen, da kein Mapping existiert!")
		return map.getNonNullOrException(key1, key2);
	}

	/**
	 * Versucht des Mapping (K1, K2) zu löschen.
	 * Wirft eine DeveloperNotificationException, falls das Mapping(K1, K2) nicht existiert.
	 *
	 * @param <K1>  Der Typ des 1. Schlüssels.
	 * @param <K2>  Der Typ des 2. Schlüssels.
	 * @param <V>   Der Typ des zugeordneten Wertes.
	 * @param map   Die Map.
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 *
	 * @throws DeveloperNotificationException falls das Mapping(K1, K2) nicht existiert.
	 */
	public static ifMap2DRemoveFailes<K1, K2, V>(map : HashMap2D<K1, K2, V>, key1 : K1, key2 : K2) : void {
		if (!map.contains(key1, key2))
			throw new DeveloperNotificationException("GET von (" + key1 + ", " + key2 + ") fehlgeschlagen, da kein Mapping existiert!")
		map.removeOrException(key1, key2);
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
	public static ifMap3DGetIsNull<K1, K2, K3, V>(map : HashMap3D<K1, K2, K3, V>, key1 : K1, key2 : K2, key3 : K3) : V {
		if (!map.contains(key1, key2, key3))
			throw new DeveloperNotificationException("GET von (" + key1 + ", " + key2 + ", " + key3 + ") fehlgeschlagen, da kein Mapping existiert!")
		return map.getNonNullOrException(key1, key2, key3);
	}

	/**
	 * Überprüft, ob eins Liste ein Element nicht enthält, dann wird eine DeveloperNotificationException geworfen.
	 *
	 * @param <E>      Der Typ der Elemente der Liste
	 * @param listName Der Name der Liste.
	 * @param list     Die Liste.
	 * @param value    Das Element, welches vorkommen muss.
	 *
	 * @throws DeveloperNotificationException falls das Element bereits in der Liste existiert.
	 */
	public static ifListNotContains<E>(listName : string, list : List<E>, value : E) : void {
		if (!list.contains(value))
			throw new DeveloperNotificationException(listName! + " hat kein Element " + value + "!")
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
	public static ifListAddsDuplicate<E>(listName : string, list : List<E>, value : E) : void {
		if (list.contains(value))
			throw new DeveloperNotificationException(listName! + " hat bereits das Element " + value + "!")
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
	public static ifListRemoveFailes<E>(listName : string, list : List<E>, value : E) : void {
		if (!list.remove(value))
			throw new DeveloperNotificationException(listName! + " konnte Element " + value + " nicht entfernen!")
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
	public static ifListGetFirstFailes<E>(listName : string, list : List<E>) : E {
		if (list.isEmpty())
			throw new DeveloperNotificationException(listName! + " hat kein erstes Element!")
		const first : E | null = list.get(0);
		if (first === null)
			throw new DeveloperNotificationException(listName! + " hat zwar ein erstes Element, aber es ist NULL!")
		return first;
	}

	/**
	 * Liefert das letzte NICHT-NULL Element der Liste.
	 *
	 * @param <E>      Der Typ der Elemente der Liste
	 * @param listName Der Name der Liste.
	 * @param list     Die Liste.
	 *
	 * @return das letzte NICHT-NULL Element der Liste.
	 *
	 * @throws DeveloperNotificationException falls kein letztes NICHT-NULL Element existiert.
	 */
	public static ifListGetLastFailes<E>(listName : string, list : List<E>) : E {
		if (list.isEmpty())
			throw new DeveloperNotificationException(listName! + " hat kein letztes Element!")
		const last : E | null = list.get(list.size() - 1);
		if (last === null)
			throw new DeveloperNotificationException(listName! + " hat zwar ein letztes Element, aber es ist NULL!")
		return last;
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
	public static ifCollectionIsEmpty<E>(listName : string, collection : Collection<E>) : void {
		if (collection.isEmpty())
			throw new DeveloperNotificationException("Die Liste '" + listName! + "' darf nicht leer sein!")
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
	public static ifSetAddsDuplicate<E>(setName : string, set : JavaSet<E>, value : E) : void {
		if (!set.add(value))
			throw new DeveloperNotificationException(setName! + " hat bereits das Element " + value + "!")
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
	public static ifSetRemoveFailes<E>(setName : string, set : JavaSet<E>, value : E) : void {
		if (!set.remove(value))
			throw new DeveloperNotificationException(setName! + " konnte Element " + value + " nicht entfernen!")
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
	public static ifSetContains<E>(setName : string, set : JavaSet<E>, value : E) : void {
		if (set.contains(value))
			throw new DeveloperNotificationException(setName! + " hat darf " + value + " nicht enthalten!")
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
	public static ifSetNotContains<E>(setName : string, set : JavaSet<E>, value : E) : void {
		if (!set.contains(value))
			throw new DeveloperNotificationException(setName! + " muss " + value + " enthalten!")
	}

	/**
	 * Liefert die Zahl des umgewandelten Strings.
	 *
	 * @param s Der String der in ein int umgewandelt werden soll.
	 *
	 * @return die Zahl des umgewandelten Strings.
	 */
	public static ifNotInt(s : string | null) : number {
		if (s === null)
			throw new DeveloperNotificationException("NULL-String kann nicht in eine Zahl umgwandelt werden")
		return JavaInteger.parseInt(s);
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
	public static ifArrayIsEmpty<E>(arrayName : string, values : Array<E | null>) : void {
		if (values.length === 0)
			throw new DeveloperNotificationException("Das Array '" + arrayName! + "' darf nicht leer sein!")
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Throwable', 'de.svws_nrw.core.exceptions.DeveloperNotificationException', 'java.lang.RuntimeException', 'java.lang.Exception', 'java.io.Serializable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_exceptions_DeveloperNotificationException(obj : unknown) : DeveloperNotificationException {
	return obj as DeveloperNotificationException;
}
