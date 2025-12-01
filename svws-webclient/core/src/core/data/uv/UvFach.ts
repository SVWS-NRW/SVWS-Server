import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvFach extends JavaObject {

	/**
	 * Die ID des UV-Fachs.
	 */
	public id: number = -1;

	/**
	 * Die ID des Faches (Fremdschlüssel auf die Tabelle EigeneSchule_Faecher).
	 */
	public idFach: number = -1;

	/**
	 * Das Datum, ab dem das Fach gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis wann das Fach gültig ist (falls vorhanden).
	 */
	public gueltigBis: string | null = null;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvFach')))) && (this.id === (another as unknown as UvFach).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung des UV-Fachs
	 */
	public toString(): string | null {
		return this.id + "-" + this.idFach + "-" + this.gueltigVon;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvFach'].includes(name);
	}

	public static readonly class = new Class<UvFach>('de.svws_nrw.core.data.uv.UvFach');

	public static transpilerFromJSON(json: string): UvFach {
		const obj = JSON.parse(json) as Partial<UvFach>;
		const result = new UvFach();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj: UvFach): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvFach>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvFach(obj: unknown): UvFach {
	return obj as UvFach;
}
