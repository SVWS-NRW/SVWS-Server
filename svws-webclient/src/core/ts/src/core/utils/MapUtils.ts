import { JavaObject } from '../../java/lang/JavaObject';
import { JavaSet } from '../../java/util/JavaSet';
import { JavaMap } from '../../java/util/JavaMap';
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
		const setNeu : HashSet<V> = new HashSet();
		map.put(key, setNeu);
		return setNeu;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.MapUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_MapUtils(obj : unknown) : MapUtils {
	return obj as MapUtils;
}
