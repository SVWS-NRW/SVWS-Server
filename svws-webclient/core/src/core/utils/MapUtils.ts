import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import type { JavaMap } from '../../java/util/JavaMap';
import { HashSet } from '../../java/util/HashSet';

export class MapUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert das "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K>  Der Typ der Schlüssel.
	 * @param <V>  Der Typ des Objekte im Set.
	 * @param map  Die Map, welche K auf "Set of V" abbildet.
	 * @param key  Der Schlüssel.
	 *
	 * @return das "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static getOrCreateHashSet<K, V>(map : JavaMap<K, JavaSet<V>>, key : K) : JavaSet<V> {
		const set : JavaSet<V> | null = map.get(key);
		if (set !== null)
			return set;
		const setNeu : HashSet<V> = new HashSet<V>();
		map.put(key, setNeu);
		return setNeu;
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K>  Der Typ der Schlüssel.
	 * @param <V>  Der Typ der Objekte in der ArrayList.
	 * @param map  Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key  Der Schlüssel.
	 *
	 * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static getOrCreateArrayList<K, V>(map : JavaMap<K, List<V>>, key : K) : List<V> {
		const list : List<V> | null = map.get(key);
		if (list !== null)
			return list;
		const listNeu : ArrayList<V> = new ArrayList<V>();
		map.put(key, listNeu);
		return listNeu;
	}

	/**
	 * Fügt der dem Schlüssel K zugeordneten Liste den Wert V hinzu, falls dieser nicht bereits existiert.
	 * Erzeugt eine zugeordnete Liste, falls diese noch nicht existiert.
	 *
	 * @param <K>    Der Typ der Schlüssel.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map    Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key    Der Schlüssel.
	 * @param value  Der Wert, welcher der Liste der Liste hinzugefügt werden soll.
	 */
	public static addToListIfNotExists<K, V>(map : JavaMap<K, List<V>>, key : K, value : V) : void {
		const list : List<V> | null = map.get(key);
		if (list !== null) {
			if (!list.contains(value))
				list.add(value);
		} else {
			const listNeu : List<V> | null = new ArrayList<V>();
			listNeu.add(value);
			map.put(key, listNeu);
		}
	}

	/**
	 * Fügt der dem Schlüssel K zugeordneten Liste den Wert V hinzu.
	 * Erzeugt eine zugeordnete Liste, falls diese noch nicht existiert.
	 *
	 * @param <K>    Der Typ der Schlüssel.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map    Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key    Der Schlüssel.
	 * @param value  Der Wert, welcher der Liste der Liste hinzugefügt werden soll.
	 */
	public static addToList<K, V>(map : JavaMap<K, List<V>>, key : K, value : V) : void {
		const list : List<V> | null = map.get(key);
		if (list !== null) {
			list.add(value);
		} else {
			const listNeu : List<V> | null = new ArrayList<V>();
			listNeu.add(value);
			map.put(key, listNeu);
		}
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K>    Der Typ des 1. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map    Die Map, welche K auf "ArrayList of V" abbildet.
	 * @param key    Der 1. Schlüssel.
	 * @param value  Der Wert, welcher aus der Liste von (K1, K2) entfernt werden soll.
	 */
	public static removeFromListAndTrimOrException<K, V>(map : JavaMap<K, List<V>>, key : K, value : V) : void {
		const list : List<V> | null = DeveloperNotificationException.ifMapGetIsNull(map, key);
		DeveloperNotificationException.ifListRemoveFailes("list", list, value);
		if (list.isEmpty())
			DeveloperNotificationException.ifMapRemoveFailes(map, key);
	}

	/**
	 * Liefert den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 *
	 * @param <K>          Der Typ der Schlüssel.
	 * @param <V>          Der Typ der Objekte in der Map.
	 * @param map          Die Map, welche K auf V abbildet.
	 * @param key          Der Schlüssel.
	 * @param defaultValue Der Default Wert, falls kein Mapping existiert.
	 *
	 * @return den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 */
	public static getOrDefault<K, V>(map : JavaMap<K, V>, key : K, defaultValue : V) : V {
		const value : V | null = map.get(key);
		if (value === null)
			return defaultValue;
		return value;
	}

	/**
	 * Falls der Schlüssel K keinen zugeordneten Wert hat, wird der übergebene Wert hinzugefügt.
	 *
	 * @param <K>    Der Typ der Schlüssel.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 *
	 * @param map    Die Map, welche K auf V abbildet.
	 * @param key    Der Schlüssel.
	 * @param value  Der Wert, welcher hinzugefügt werden soll, falls es noch keine Zuordnung gibt.
	 */
	public static putNonNullIfNotExists<K, V>(map : JavaMap<K, V>, key : K, value : V) : void {
		if (map.containsKey(key))
			return;
		map.put(key, value);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.MapUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.MapUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_MapUtils(obj : unknown) : MapUtils {
	return obj as MapUtils;
}
