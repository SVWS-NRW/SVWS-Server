import { JavaObject } from '../../java/lang/JavaObject';
import { HashMap2D } from '../../core/adt/map/HashMap2D';
import type { JavaSet } from '../../java/util/JavaSet';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { HashSet } from '../../java/util/HashSet';

export class Map2DUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static getOrCreateArrayList<K1, K2, V>(map2D : HashMap2D<K1, K2, List<V>>, key1 : K1, key2 : K2) : List<V> {
		const list : List<V> | null = map2D.getOrNull(key1, key2);
		if (list !== null)
			return list;
		const listNeu : ArrayList<V> = new ArrayList<V>();
		map2D.put(key1, key2, listNeu);
		return listNeu;
	}

	/**
	 * Liefert die "HashSet of V" des Schlüssels. Erstellt eine leere "HashSet of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "HashSet of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 *
	 * @return die "HashSet of V" des Schlüssels. Erstellt eine leere "HashSet of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static getOrCreateHashSet<K1, K2, V>(map2D : HashMap2D<K1, K2, JavaSet<V>>, key1 : K1, key2 : K2) : JavaSet<V> {
		const set : JavaSet<V> | null = map2D.getOrNull(key1, key2);
		if (set !== null)
			return set;
		const setNeu : HashSet<V> = new HashSet<V>();
		map2D.put(key1, key2, setNeu);
		return setNeu;
	}

	/**
	 * Liefert den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 *
	 * @param <K1>         Der Typ des 1. Schlüssels.
	 * @param <K2>         Der Typ des 2. Schlüssels.
	 * @param <V>          Der Typ der Objekte in der Map.
	 * @param map2D        Die Map, welche (K1, K2) auf V abbildet.
	 * @param key1         Der 1. Schlüssel.
	 * @param key2         Der 2. Schlüssel.
	 * @param defaultValue Der Default Wert, falls kein Mapping existiert.
	 *
	 * @return den Wert V des Schlüssels K, falls diese existiert, andernfalls den Default-Wert.
	 */
	public static getOrDefault<K1, K2, V>(map2D : HashMap2D<K1, K2, V>, key1 : K1, key2 : K2, defaultValue : V) : V {
		const value : V | null = map2D.getOrNull(key1, key2);
		if (value === null)
			return defaultValue;
		return value;
	}

	/**
	 * Fügt der dem Schlüssel (K1, K2) zugeordneten Liste den Wert V hinzu.
	 * Erzeugt eine Liste, falls noch keine existiert.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der Wert, welcher der zugeordneten Liste hinzugefügt wird.
	 */
	public static addToList<K1, K2, V>(map2D : HashMap2D<K1, K2, List<V>>, key1 : K1, key2 : K2, value : V) : void {
		const list : List<V> | null = map2D.getOrNull(key1, key2);
		if (list !== null) {
			list.add(value);
		} else {
			const listNeu : ArrayList<V> = new ArrayList<V>();
			listNeu.add(value);
			map2D.put(key1, key2, listNeu);
		}
	}

	/**
	 * Fügt der dem Schlüssel (K1, K2) zugeordneten Liste den Wert V hinzu, falls dieser noch nicht existiert.
	 * Erzeugt eine Liste, falls noch keine existiert.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der Wert, welcher der zugeordneten Liste hinzugefügt wird.
	 */
	public static addToListIfNotExists<K1, K2, V>(map2D : HashMap2D<K1, K2, List<V>>, key1 : K1, key2 : K2, value : V) : void {
		const list : List<V> | null = map2D.getOrNull(key1, key2);
		if (list !== null) {
			if (!list.contains(value))
				list.add(value);
		} else {
			const listNeu : ArrayList<V> = new ArrayList<V>();
			listNeu.add(value);
			map2D.put(key1, key2, listNeu);
		}
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>   Der Typ des 1. Schlüssels.
	 * @param <K2>   Der Typ des 2. Schlüssels.
	 * @param <V>    Der Typ der Objekte in der ArrayList.
	 * @param map2D  Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1   Der 1. Schlüssel.
	 * @param key2   Der 2. Schlüssel.
	 * @param value  Der Wert, welcher aus der Liste von (K1, K2) entfernt werden soll.
	 */
	public static removeFromListAndTrimOrException<K1, K2, V>(map2D : HashMap2D<K1, K2, List<V>>, key1 : K1, key2 : K2, value : V) : void {
		const list : List<V> | null = map2D.getOrException(key1, key2);
		DeveloperNotificationException.ifListRemoveFailes("list", list, value);
		if (list.isEmpty())
			map2D.removeOrException(key1, key2);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.Map2DUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.Map2DUtils'].includes(name);
	}

	public static class = new Class<Map2DUtils>('de.svws_nrw.core.utils.Map2DUtils');

}

export function cast_de_svws_nrw_core_utils_Map2DUtils(obj : unknown) : Map2DUtils {
	return obj as Map2DUtils;
}
