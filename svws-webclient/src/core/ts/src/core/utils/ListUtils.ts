import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import type { Predicate } from '../../java/util/function/Predicate';

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

	/**
	 * Liefert eine permutierte Kopie des Arrays als Liste.
	 *
	 * @param <T>      Der Inhaltstyp der Liste.
	 * @param arrayOfT Das Array mit allen Elementen vom Typ T.
	 * @param random   Ein {@link Random}-Objekt zum Permutieren der Elemente des Arrays.
	 *
	 * @return eine permutierte Kopie des Arrays als Liste.
	 */
	public static getCopyAsArrayListPermuted<T>(arrayOfT : Array<T>, random : Random) : List<T> {
		const list : List<T> = new ArrayList();
		const perm : Array<number> | null = Array(arrayOfT.length).fill(0);
		for (let i : number = 0; i < perm.length; i++)
			perm[i] = i;
		for (let i1 : number = 0; i1 < perm.length; i1++) {
			const i2 : number = random.nextInt(perm.length);
			const save1 : number = perm[i1];
			const save2 : number = perm[i2];
			perm[i1] = save2;
			perm[i2] = save1;
		}
		for (let i : number = 0; i < arrayOfT.length; i++)
			list.add(arrayOfT[perm[i]]);
		return list;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.ListUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_ListUtils(obj : unknown) : ListUtils {
	return obj as ListUtils;
}
