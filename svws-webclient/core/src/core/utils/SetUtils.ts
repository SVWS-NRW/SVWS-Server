import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { HashSet } from '../../java/util/HashSet';

export class SetUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert ein Set (HashSet), welches mit einem Element gefüllt wurde.
	 *
	 * @param <E>      Der Inhaltstyp der Liste.
	 * @param element  Das Element, welches hinzugefügt wird.
	 *
	 * @return ein Set, welches mit einem Element gefüllt wurde.
	 */
	public static create1<E>(element : E) : JavaSet<E> {
		const set : HashSet<E> = new HashSet<E>();
		set.add(element);
		return set;
	}

	/**
	 * Liefert ein Set (HashSet), welches mit einem Element gefüllt wurde.
	 *
	 * @param <E>       Der Inhaltstyp der Liste.
	 * @param element1  Ein Element, welches hinzugefügt wird.
	 * @param element2  Ein Element, welches hinzugefügt wird.
	 *
	 * @return ein Set, welches mit einem Element gefüllt wurde.
	 */
	public static create2<E>(element1 : E, element2 : E) : JavaSet<E> {
		const set : HashSet<E> = new HashSet<E>();
		set.add(element1);
		set.add(element2);
		return set;
	}

	/**
	 * Liefert ein Set (HashSet), welches mit den Elementen der Liste gefüllt wurde.
	 *
	 * @param <E>   Der Inhaltstyp der Liste.
	 * @param list  Die Liste der Elemente.
	 *
	 * @return ein Set (HashSet), welches mit den Elementen der Liste gefüllt wurde.
	 */
	public static createFromList<E>(list : List<E>) : JavaSet<E> {
		const set : HashSet<E> = new HashSet<E>();
		set.addAll(list);
		return set;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.SetUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.SetUtils'].includes(name);
	}

	public static class = new Class<SetUtils>('de.svws_nrw.core.utils.SetUtils');

}

export function cast_de_svws_nrw_core_utils_SetUtils(obj : unknown) : SetUtils {
	return obj as SetUtils;
}
