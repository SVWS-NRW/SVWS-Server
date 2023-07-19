import { JavaObject } from '../../java/lang/JavaObject';
import { HashMap2D } from '../../core/adt/map/HashMap2D';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';

export class Map2DUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>  Der Typ des 1. Schlüssels.
	 * @param <K2>  Der Typ des 2. Schlüssels.
	 * @param <V>   Der Typ der Objekte in der ArrayList.
	 * @param map2D   Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 *
	 * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static getOrCreateArrayList<K1, K2, V>(map2D : HashMap2D<K1, K2, List<V>>, key1 : K1, key2 : K2) : List<V> {
		const list : List<V> | null = map2D.getOrNull(key1, key2);
		if (list !== null)
			return list;
		const listNeu : ArrayList<V> = new ArrayList();
		map2D.put(key1, key2, listNeu);
		return listNeu;
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
		const list : List<V> | null = map2D.getNonNullOrException(key1, key2);
		DeveloperNotificationException.ifListRemoveFailes("list", list, value);
		if (list.isEmpty())
			map2D.removeOrException(key1, key2);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.Map2DUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_Map2DUtils(obj : unknown) : Map2DUtils {
	return obj as Map2DUtils;
}
