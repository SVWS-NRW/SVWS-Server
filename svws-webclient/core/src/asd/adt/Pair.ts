import { JavaObject } from '../../java/lang/JavaObject';
import { Class } from '../../java/lang/Class';

export class Pair<A, B> extends JavaObject {

	/**
	 * Der erster Wert des Paares
	 */
	public a : A;

	/**
	 * Der zweite Wert des Paares
	 */
	public b : B | null = null;


	/**
	 * Erstellt ein neues Paar.
	 *
	 * @param a   der erste Wert des Paares
	 * @param b   der zweite Wert des Paares
	 */
	public constructor(a : A, b : B | null) {
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
		if (!(((o instanceof JavaObject) && (o.isTranspiledInstanceOf('de.svws_nrw.asd.adt.Pair')))))
			return false;
		const e : Pair<any, any> | null = cast_de_svws_nrw_asd_adt_Pair(o);
		const a_equals : boolean = JavaObject.equalsTranspiler(this.a, (e.a));
		const b_equals : boolean = (this.b === null) ? (e.b === null) : JavaObject.equalsTranspiler(this.b, (e.b));
		return a_equals && b_equals;
	}

	public hashCode() : number {
		return JavaObject.getTranspilerHashCode(this.a) ^ ((this.b === null) ? 0 : JavaObject.getTranspilerHashCode(this.b));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.adt.Pair';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.adt.Pair'].includes(name);
	}

	public static class = new Class<Pair<any, any>>('de.svws_nrw.asd.adt.Pair');

}

export function cast_de_svws_nrw_asd_adt_Pair<A, B>(obj : unknown) : Pair<A, B> {
	return obj as Pair<A, B>;
}
