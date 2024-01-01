import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { HashMap3D } from '../../core/adt/map/HashMap3D';
import { HashSet } from '../../java/util/HashSet';

export class Map3DUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>  Der Typ des 1. Schlüssels.
	 * @param <K2>  Der Typ des 2. Schlüssels.
	 * @param <K3>  Der Typ des 3. Schlüssels.
	 * @param <V>   Der Typ der Objekte in der ArrayList.
	 * @param map3D   Die Map, welche (K1, K2, K3) auf "ArrayList of V" abbildet.
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 *
	 * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static getOrCreateArrayList<K1, K2, K3, V>(map3D : HashMap3D<K1, K2, K3, List<V>>, key1 : K1, key2 : K2, key3 : K3) : List<V> {
		const list : List<V> | null = map3D.getOrNull(key1, key2, key3);
		if (list !== null)
			return list;
		const listNeu : ArrayList<V> = new ArrayList();
		map3D.put(key1, key2, key3, listNeu);
		return listNeu;
	}

	/**
	 * Liefert das "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
	 *
	 * @param <K1>  Der Typ des 1. Schlüssels.
	 * @param <K2>  Der Typ des 2. Schlüssels.
	 * @param <K3>  Der Typ des 3. Schlüssels.
	 * @param <V>   Der Typ der Objekte in dem HashSet.
	 * @param map3D   Die Map, welche (K1, K2, K3) auf "Set of V" abbildet.
	 * @param key1  Der 1. Schlüssel.
	 * @param key2  Der 2. Schlüssel.
	 * @param key3  Der 3. Schlüssel.
	 *
	 * @return die "Set of V" des Schlüssels. Erstellt ein leeres "Set of V", falls eine solche Zuordnung nicht existierte.
	 */
	public static getOrCreateSet<K1, K2, K3, V>(map3D : HashMap3D<K1, K2, K3, JavaSet<V>>, key1 : K1, key2 : K2, key3 : K3) : JavaSet<V> {
		const set : JavaSet<V> | null = map3D.getOrNull(key1, key2, key3);
		if (set !== null)
			return set;
		const setNeu : HashSet<V> = new HashSet();
		map3D.put(key1, key2, key3, setNeu);
		return setNeu;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.Map3DUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.Map3DUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_Map3DUtils(obj : unknown) : Map3DUtils {
	return obj as Map3DUtils;
}
