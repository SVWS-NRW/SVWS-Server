import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { ArrayList } from '../../java/util/ArrayList';
import type { Collection } from '../../java/util/Collection';
import type { List } from '../../java/util/List';
import type { JavaMap } from '../../java/util/JavaMap';
import { HashSet } from '../../java/util/HashSet';
import type { Predicate } from '../../java/util/function/Predicate';

export class CollectionUtils extends JavaObject {


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

	/**
	 * Liefert eine gefilterte {@link Collection} und liefert die Ergebnismenge in einem {@link Set}.
	 *
	 * @param <E>       Der Typ der Elemente in der {@link Collection}.
	 * @param values    Die {@link Collection}.
	 * @param predicate Das {@link Predicate}, welches entscheidet, ob ein Element zur Ergebnismenge gehört.
	 *
	 * @return eine gefilterte {@link Collection} und liefert die Ergebnismenge in einem {@link Set}.
	 */
	public static toFilteredHashSet<E>(values : Collection<E>, predicate : Predicate<E>) : JavaSet<E> {
		const set : HashSet<E> = new HashSet();
		for (const e of values)
			if (predicate.test(e))
				set.add(e);
		return set;
	}

	/**
	 * Liefert eine gefilterte {@link Collection} und liefert die Ergebnismenge in einer {@link List}.
	 *
	 * @param <E>       Der Typ der Elemente in der {@link Collection}.
	 * @param values    Die {@link Collection}.
	 * @param predicate Das {@link Predicate}, welches entscheidet, ob ein Element zur Ergebnismenge gehört.
	 *
	 * @return eine gefilterte {@link Collection} und liefert die Ergebnismenge in einer {@link List}.
	 */
	public static toFilteredArrayList<E>(values : Collection<E>, predicate : Predicate<E>) : List<E> {
		const set : ArrayList<E> = new ArrayList();
		for (const e of values)
			if (predicate.test(e))
				set.add(e);
		return set;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.CollectionUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_CollectionUtils(obj : unknown) : CollectionUtils {
	return obj as CollectionUtils;
}
