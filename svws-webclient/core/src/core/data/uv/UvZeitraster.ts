import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvZeitraster extends JavaObject {

	/**
	 * Die ID des Zeitrasters.
	 */
	public id: number = -1;

	/**
	 * Das Datum, ab dem das Zeitraster gültig ist (ISO-Datum als String, z. B. 2025-08-01).
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis zu dem das Zeitraster gültig ist (oder null, falls unbegrenzt gültig).
	 */
	public gueltigBis: string | null = null;

	/**
	 * Die Bezeichnung des Zeitrasters.
	 */
	public bezeichnung: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvZeitraster')))) && (this.id === (another as unknown as UvZeitraster).id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung
	 */
	public toString(): string | null {
		return this.id + "-" + this.gueltigVon;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvZeitraster';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvZeitraster'].includes(name);
	}

	public static readonly class = new Class<UvZeitraster>('de.svws_nrw.core.data.uv.UvZeitraster');

	public static transpilerFromJSON(json: string): UvZeitraster {
		const obj = JSON.parse(json) as Partial<UvZeitraster>;
		const result = new UvZeitraster();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj: UvZeitraster): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvZeitraster>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvZeitraster(obj: unknown): UvZeitraster {
	return obj as UvZeitraster;
}
