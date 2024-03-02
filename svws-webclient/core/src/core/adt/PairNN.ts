import { JavaObject } from '../../java/lang/JavaObject';

export class PairNN<A, B> extends JavaObject {

	/**
	 * Der 1. Wert des Paares.
	 */
	public a : A;

	/**
	 * Der 2. Wert des Paares.
	 */
	public b : B;


	/**
	 * Erstellt ein neues Paar.
	 *
	 * @param a   der erste Wert des Paares
	 * @param b   der zweite Wert des Paares
	 */
	public constructor(a : A, b : B) {
		super();
		this.a = a;
		this.b = b;
	}

	public toString() : string {
		return "[" + this.a + ", " + this.b + "]";
	}

	public equals(o : unknown | null) : boolean {
		if (o === null)
			return false;
		if (!(((o instanceof JavaObject) && ((o as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.adt.PairNN')))))
			return false;
		const e : PairNN<any, any> | null = cast_de_svws_nrw_core_adt_PairNN(o);
		const a_equals : boolean = JavaObject.equalsTranspiler(this.a, (e.a));
		const b_equals : boolean = JavaObject.equalsTranspiler(this.b, (e.b));
		return a_equals && b_equals;
	}

	public hashCode() : number {
		return JavaObject.getTranspilerHashCode(this.a) ^ JavaObject.getTranspilerHashCode(this.b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.adt.PairNN';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.adt.PairNN'].includes(name);
	}

}

export function cast_de_svws_nrw_core_adt_PairNN<A, B>(obj : unknown) : PairNN<A, B> {
	return obj as PairNN<A, B>;
}
