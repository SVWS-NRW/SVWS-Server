import { JavaObject } from '../../../java/lang/JavaObject';
import { PairIterator, cast_de_svws_nrw_core_adt_iterator_PairIterator } from '../../../core/adt/iterator/PairIterator';
import { PairIteratorModus, cast_de_svws_nrw_core_adt_iterator_PairIteratorModus } from '../../../core/adt/iterator/PairIteratorModus';
import type { JavaIterable } from '../../../java/lang/JavaIterable';
import { cast_java_lang_Iterable } from '../../../java/lang/JavaIterable';
import { PairNN } from '../../../asd/adt/PairNN';
import { ArrayList } from '../../../java/util/ArrayList';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';

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
	public constructor(source: JavaIterable<T>, modus: PairIteratorModus);

	/**
	 * Erstellt ein neues Iterable über alle Paare des angegebenen Arrays im gewünschten Modus.
	 *
	 * @param source das Quell-Array
	 * @param modus  der gewünschte Iterationsmodus
	 */
	public constructor(source: Array<T>, modus: PairIteratorModus);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0: Array<T> | JavaIterable<T>, __param1: PairIteratorModus) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.lang.Iterable'))) || (__param0 === null)) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.adt.iterator.PairIteratorModus'))))) {
			const source: JavaIterable<T> = cast_java_lang_Iterable(__param0);
			const modus: PairIteratorModus = cast_de_svws_nrw_core_adt_iterator_PairIteratorModus(__param1);
			this.modus = modus;
			this.elemente = new ArrayList();
			for (const element of source) {
				this.elemente.add(element);
			}
		} else if (((__param0 !== undefined) && Array.isArray(__param0)) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.adt.iterator.PairIteratorModus'))))) {
			const source: Array<T> = __param0 as unknown as Array<T>;
			const modus: PairIteratorModus = cast_de_svws_nrw_core_adt_iterator_PairIteratorModus(__param1);
			this.modus = modus;
			this.elemente = Arrays.asList(...source);
		} else throw new Error('invalid method overload');
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
