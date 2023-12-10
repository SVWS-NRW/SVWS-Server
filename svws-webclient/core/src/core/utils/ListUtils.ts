import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import type { Predicate } from '../../java/util/function/Predicate';

export class ListUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert eine gefilterte Kopie der Liste.
	 *
	 * @param <E>    Der Inhaltstyp der Liste.
	 * @param list   Die zu filternde Liste.
	 * @param filter Die Funktion, welche bestimmt ob ein Objekt der Liste gefiltert werden soll.
	 *
	 * @return eine gefilterte Kopie der Liste.
	 */
	public static getCopyFiltered<E>(list : List<E>, filter : Predicate<E>) : List<E> {
		const listFiltered : ArrayList<E> = new ArrayList();
		for (const t of list)
			if (filter.test(t))
				listFiltered.add(t);
		return listFiltered;
	}

	/**
	 * Liefert die Anzahl an Elementen, die in der Liste den Filterkriterien entsprechen.
	 *
	 * @param <E>    Der Inhaltstyp der Liste.
	 * @param list   Die zu filternde Liste.
	 * @param filter Die Funktion, welche bestimmt ob ein Objekt das Kriterium erfüllt.
	 *
	 * @return die Anzahl an Elementen, die in der Liste den Filterkriterien entsprechen.
	 */
	public static getCountFiltered<E>(list : List<E>, filter : Predicate<E>) : number {
		let summe : number = 0;
		for (const t of list)
			if (filter.test(t))
				summe++;
		return summe;
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

	/**
	 * Fügt das Element der Liste hinzu, falls es nicht bereits existiert. Verhindert so Duplikate.
	 *
	 * @param <E>    Der Inhaltstyp der Liste.
	 * @param list   Die Liste in welche hinzugefügt wird.
	 * @param e      Das Element, welches ggf. hinzugefügt wird.
	 */
	public static addIfNotExists<E>(list : List<E>, e : E) : void {
		if (!list.contains(e))
			list.add(e);
	}

	/**
	 * Fügt das Element der Liste hinzu, falls es nicht bereits existiert. Verhindert so Duplikate.
	 *
	 * @param <E>        Der Inhaltstyp der Liste.
	 * @param list       Die Liste in welche hinzugefügt wird.
	 * @param listToAdd  Die Liste aus welcher kopiert wird.
	 */
	public static addAllIfNotExists<E>(list : List<E>, listToAdd : List<E>) : void {
		for (const e of listToAdd)
			if (!list.contains(e))
				list.add(e);
	}

	/**
	 * Liefert das NON NULL Element an Index i, oder eine Exception.
	 *
	 * @param <E>   Der Inhaltstyp der Liste.
	 * @param list  Die Liste.
	 * @param i     Der Index i.
	 *
	 * @return das NON NULL Element an Index i, oder eine Exception.
	 */
	public static getNonNullElementAtOrException<E>(list : List<E>, i : number) : E {
		const element : E | null = list.get(i);
		if (element === null)
			throw new DeveloperNotificationException("Kein Element bei Index " + i + "!")
		return element;
	}

	/**
	 * Liefert eine Liste, welche mit einem Element gefüllt wurde.
	 *
	 * @param <E>      Der Inhaltstyp der Liste.
	 * @param element  Das Element, welches hinzugefügt wird.
	 *
	 * @return eine Liste, welche mit einem Element gefüllt wurde.
	 */
	public static create1<E>(element : E) : List<E> {
		const list : ArrayList<E> = new ArrayList();
		list.add(element);
		return list;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.ListUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.ListUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_ListUtils(obj : unknown) : ListUtils {
	return obj as ListUtils;
}
