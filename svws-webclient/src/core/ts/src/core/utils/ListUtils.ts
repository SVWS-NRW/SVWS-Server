import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import { List } from '../../java/util/List';
import { Predicate } from '../../java/util/function/Predicate';

export class ListUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert eine gefilterte Kopie der Liste.
	 *
	 * @param <T>    Der Inhaltstyp der Liste.
	 * @param list   Die zu filternde Liste.
	 * @param filter Die Funktion, welche bestimmt ob ein Objekt der Liste gefiltert werden soll.
	 *
	 * @return eine gefilterte Kopie der Liste.
	 */
	public static getCopyFiltered<T>(list : List<T>, filter : Predicate<T>) : List<T> {
		const listFiltered : ArrayList<T> = new ArrayList();
		for (const t of list)
			if (filter.test(t))
				listFiltered.add(t);
		return listFiltered;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.ListUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_ListUtils(obj : unknown) : ListUtils {
	return obj as ListUtils;
}
