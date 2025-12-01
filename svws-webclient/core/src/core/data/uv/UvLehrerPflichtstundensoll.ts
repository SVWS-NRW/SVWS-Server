import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLehrerPflichtstundensoll extends JavaObject {

	/**
	 * Die eindeutige ID des Pflichtstundensoll-Eintrags.
	 */
	public id: number = 0;

	/**
	 * Die ID des Lehrers, auf den sich das Pflichtstundensoll bezieht.
	 */
	public idLehrer: number = 0;

	/**
	 * Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat.
	 */
	public pflichtstdSoll: number = 0;

	/**
	 * Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).
	 */
	public gueltigBis: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht dieses Objekt mit einem anderen.
	 *
	 * @param obj das zu vergleichende Objekt
	 *
	 * @return {@code true}, wenn es sich um dasselbe Objekt oder ein Objekt mit derselben ID handelt,
	 *         sonst {@code false}
	 */
	public equals(obj: unknown | null): boolean {
		return (((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll')))) && (this.id === (obj as unknown as UvLehrerPflichtstundensoll).id);
	}

	/**
	 * Erzeugt den Hashcode für dieses Objekt.
	 *
	 * @return der Hashcode basierend auf der ID
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation dieses Objektes zurück.
	 *
	 * @return eine kurze textuelle Darstellung mit ID, Lehrer-ID, Pflichtstundensoll und Gültigkeitsbeginn
	 */
	public toString(): string | null {
		return this.id + "-" + this.idLehrer + "-" + this.gueltigVon;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll'].includes(name);
	}

	public static readonly class = new Class<UvLehrerPflichtstundensoll>('de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll');

	public static transpilerFromJSON(json: string): UvLehrerPflichtstundensoll {
		const obj = JSON.parse(json) as Partial<UvLehrerPflichtstundensoll>;
		const result = new UvLehrerPflichtstundensoll();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.pflichtstdSoll === undefined)
			throw new Error('invalid json format, missing attribute pflichtstdSoll');
		result.pflichtstdSoll = obj.pflichtstdSoll;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		if (obj.gueltigBis === undefined)
			throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj: UvLehrerPflichtstundensoll): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"pflichtstdSoll" : ' + obj.pflichtstdSoll.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLehrerPflichtstundensoll>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.pflichtstdSoll !== undefined) {
			result += '"pflichtstdSoll" : ' + obj.pflichtstdSoll.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLehrerPflichtstundensoll(obj: unknown): UvLehrerPflichtstundensoll {
	return obj as UvLehrerPflichtstundensoll;
}
