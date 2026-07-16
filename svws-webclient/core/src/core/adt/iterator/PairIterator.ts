import { JavaObject } from '../../../java/lang/JavaObject';
import { PairIteratorModus } from '../../../core/adt/iterator/PairIteratorModus';
import { PairNN } from '../../../asd/adt/PairNN';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { NoSuchElementException } from '../../../java/util/NoSuchElementException';
import { UnsupportedOperationException } from '../../../java/lang/UnsupportedOperationException';

export class PairIterator<T> extends JavaObject implements JavaIterator<PairNN<T, T>> {

	/**
	 * Die interne Liste der Elemente.
	 */
	private readonly elemente: List<T>;

	/**
	 * Der Iterationsmodus, der bestimmt, welche Paare geliefert werden.
	 */
	private readonly modus: PairIteratorModus;

	/**
	 * Der aktuelle Index des ersten Elements des nächsten Paares.
	 */
	private i: number = 0;

	/**
	 * Der aktuelle Index des zweiten Elements des nächsten Paares.
	 */
	private j: number = 0;

	/**
	 * Das nächste Paar, das von {@link #next()} geliefert wird, oder {@code null}.
	 */
	private nextElement: PairNN<T, T> | null = null;


	/**
	 * Erstellt einen neuen Iterator über alle Paare der angegebenen Liste im gewünschten Modus.
	 *
	 * @param elemente   die interne Liste der Elemente
	 * @param modus      der gewünschte Iterationsmodus
	 */
	constructor(elemente: List<T>, modus: PairIteratorModus) {
		super();
		this.elemente = elemente;
		this.modus = modus;
		this.i = 0;
		this.j = 0;
		this.nextElement = null;
		if (modus as unknown === PairIteratorModus.LOWER_ONLY as unknown) {
			this.j = 1;
		}
		this.calculateNextElement();
	}

	/**
	 * Diese Operation wird nicht unterstützt.
	 *
	 * @throws UnsupportedOperationException immer
	 */
	public remove(): void {
		throw new UnsupportedOperationException();
	}

	/**
	 * Liefert {@code true}, falls der Iterator mindestens ein weiteres Paar bereithält.
	 *
	 * @return {@code true}, falls der Iterator mindestens ein weiteres Paar bereithält.
	 */
	public hasNext(): boolean {
		return this.nextElement !== null;
	}

	/**
	 * Liefert das nächste Paar und rückt den Iterator einen Schritt vor.
	 *
	 * @return das nächste Paar und rückt den Iterator einen Schritt vor.
	 */
	public next(): PairNN<T, T> {
		if (this.nextElement === null) {
			throw new NoSuchElementException();
		}
		const result: PairNN<T, T> | null = this.nextElement;
		this.calculateNextElement();
		return result;
	}

	/**
	 * Berechnet das nächste Paar und speichert es in {@link #nextElement}.
	 * Wenn kein weiteres Paar existiert, wird {@link #nextElement} auf {@code null} gesetzt.
	 */
	private calculateNextElement(): void {
		this.nextElement = null;
		while (this.i < this.elemente.size()) {
			if (this.j >= this.elemente.size()) {
				this.i++;
				this.j = (this.modus as unknown === PairIteratorModus.LOWER_ONLY as unknown) ? (this.i + 1) : 0;
				continue;
			}
			const skip: boolean = ((this.modus as unknown === PairIteratorModus.NO_EQUAL as unknown) && (this.i === this.j)) || ((this.modus as unknown === PairIteratorModus.LOWER_ONLY as unknown) && (this.i >= this.j));
			if (!skip) {
				this.nextElement = new PairNN(this.elemente.get(this.i), this.elemente.get(this.j));
				this.j++;
				return;
			}
			this.j++;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.iterator.PairIterator';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['java.util.Iterator', 'de.svws_nrw.core.adt.iterator.PairIterator'].includes(name);
	}

	public static readonly class = new Class<PairIterator<any>>('de.svws_nrw.core.adt.iterator.PairIterator');

}

export function cast_de_svws_nrw_core_adt_iterator_PairIterator<T>(obj: unknown): PairIterator<T> {
	return obj as PairIterator<T>;
}
