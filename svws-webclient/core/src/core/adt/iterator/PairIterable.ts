import { JavaObject } from '../../../java/lang/JavaObject';
import { PairIterator } from '../../../core/adt/iterator/PairIterator';
import { PairIteratorModus } from '../../../core/adt/iterator/PairIteratorModus';
import type { JavaIterable } from '../../../java/lang/JavaIterable';
import { PairNN } from '../../../asd/adt/PairNN';
import { ArrayList } from '../../../java/util/ArrayList';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class PairIterable<T> extends JavaObject implements JavaIterable<PairNN<T, T>> {

	/**
	 * Die interne Liste der Elemente, die aus der Quell-Collection befüllt wird.
	 */
	private readonly elemente: List<T>;

	/**
	 * Der Iterationsmodus, der bestimmt, welche Paare geliefert werden.
	 */
	private readonly modus: PairIteratorModus;


	/**
	 * Erstellt ein neues Iterable über alle Paare der angegebenen Collection im gewünschten Modus.
	 *
	 * @param source   die Quell-Collection (List, Set oder jedes Iterable)
	 * @param modus    der gewünschte Iterationsmodus
	 */
	public constructor(source: JavaIterable<T>, modus: PairIteratorModus) {
		super();
		this.modus = modus;
		this.elemente = new ArrayList();
		for (const element of source) {
			this.elemente.add(element);
		}
	}

	/**
	 * Liefert einen neuen Iterator über alle Paare.
	 *
	 * @return einen neuen Iterator über alle Paare.
	 */
	public iterator(): JavaIterator<PairNN<T, T>> {
		return new PairIterator<T>(this.elemente, this.modus);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.iterator.PairIterable';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.adt.iterator.PairIterable', 'java.lang.Iterable'].includes(name);
	}

	public static readonly class = new Class<PairIterable<any>>('de.svws_nrw.core.adt.iterator.PairIterable');

	public [Symbol.iterator](): Iterator<PairNN<T, T>> {
		const iter: JavaIterator<PairNN<T, T>> = this.iterator();
		const result: Iterator<PairNN<T, T>> = {
			next(): IteratorResult<PairNN<T, T>> {
				if (iter.hasNext())
					return { value : iter.next(), done : false };
				return { value : null, done : true };
			}
		};
		return result;
	}

}

export function cast_de_svws_nrw_core_adt_iterator_PairIterable<T>(obj: unknown): PairIterable<T> {
	return obj as PairIterable<T>;
}
