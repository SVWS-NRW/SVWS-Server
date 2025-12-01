import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LongPair extends JavaObject {

	/**
	 *  Der erste long-Wert.
	 */
	public a: number = 0;

	/**
	 *  Der zweite long-Wert.
	 */
	public b: number = 0;


	/**
	 * Erzeugt ein neues LongPair-Objekt mit den übergebenen Werten.
	 *
	 * @param a der erste long-Wert
	 * @param b der zweite long-Wert
	 */
	public constructor(a: number, b: number);

	/**
	 * Default-Konstruktor für JSON-Serialisierung
	 */
	public constructor();

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0?: number, __param1?: number) {
		super();
		if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && typeof __param1 === "number")) {
			const a: number = __param0 as number;
			const b: number = __param1 as number;
			this.a = a;
			this.b = b;
		} else if ((__param0 === undefined) && (__param1 === undefined)) {
			// empty method body
		} else throw new Error('invalid method overload');
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.LongPair')))) && (this.a === (another as unknown as LongPair).a) && (this.b === (another as unknown as LongPair).b);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den Hashcode
	 */
	public hashCode(): number {
		return (31 * JavaLong.hashCode((this.a))) + JavaLong.hashCode((this.b));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return eine lesbare Beschreibung des Objekts
	 */
	public toString(): string | null {
		return this.a + "-" + this.b;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.LongPair';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.LongPair'].includes(name);
	}

	public static readonly class = new Class<LongPair>('de.svws_nrw.core.data.uv.LongPair');

	public static transpilerFromJSON(json: string): LongPair {
		const obj = JSON.parse(json) as Partial<LongPair>;
		const result = new LongPair();
		if (obj.a === undefined)
			throw new Error('invalid json format, missing attribute a');
		result.a = obj.a;
		if (obj.b === undefined)
			throw new Error('invalid json format, missing attribute b');
		result.b = obj.b;
		return result;
	}

	public static transpilerToJSON(obj: LongPair): string {
		let result = '{';
		result += '"a" : ' + obj.a.toString() + ',';
		result += '"b" : ' + obj.b.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LongPair>): string {
		let result = '{';
		if (obj.a !== undefined) {
			result += '"a" : ' + obj.a.toString() + ',';
		}
		if (obj.b !== undefined) {
			result += '"b" : ' + obj.b.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_LongPair(obj: unknown): LongPair {
	return obj as LongPair;
}
