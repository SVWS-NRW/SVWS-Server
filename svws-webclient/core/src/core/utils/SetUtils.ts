import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
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
		const list : HashSet<E> = new HashSet();
		list.add(element);
		return list;
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
		const list : HashSet<E> = new HashSet();
		list.add(element1);
		list.add(element2);
		return list;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.SetUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.SetUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_SetUtils(obj : unknown) : SetUtils {
	return obj as SetUtils;
}
