import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchulgliederungGueltigerAbschluss extends JavaObject {

	/**
	 * Das Kürzel des allgemeinbildenden Abschlusses
	 */
	public allgemeinbildend : string = "OA";

	/**
	 * Das Kürzel des berufsbildenden Abschlusses
	 */
	public berufsbildend : string = "OA";

	/**
	 * der niedrigste Jahrgang, in dem der Abschluss möglich ist
	 */
	public jahrgangVon : string = "JAHRGANG_01";

	/**
	 * der höchste Jahrgang, in dem der Abschluss möglich ist
	 */
	public jahrgangBis : string = "JAHRGANG_13";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.SchulgliederungGueltigerAbschluss';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.SchulgliederungGueltigerAbschluss'].includes(name);
	}

	public static class = new Class<SchulgliederungGueltigerAbschluss>('de.svws_nrw.asd.data.schule.SchulgliederungGueltigerAbschluss');

	public static transpilerFromJSON(json : string): SchulgliederungGueltigerAbschluss {
		const obj = JSON.parse(json) as Partial<SchulgliederungGueltigerAbschluss>;
		const result = new SchulgliederungGueltigerAbschluss();
		if (obj.allgemeinbildend === undefined)
			throw new Error('invalid json format, missing attribute allgemeinbildend');
		result.allgemeinbildend = obj.allgemeinbildend;
		if (obj.berufsbildend === undefined)
			throw new Error('invalid json format, missing attribute berufsbildend');
		result.berufsbildend = obj.berufsbildend;
		if (obj.jahrgangVon === undefined)
			throw new Error('invalid json format, missing attribute jahrgangVon');
		result.jahrgangVon = obj.jahrgangVon;
		if (obj.jahrgangBis === undefined)
			throw new Error('invalid json format, missing attribute jahrgangBis');
		result.jahrgangBis = obj.jahrgangBis;
		return result;
	}

	public static transpilerToJSON(obj : SchulgliederungGueltigerAbschluss) : string {
		let result = '{';
		result += '"allgemeinbildend" : ' + JSON.stringify(obj.allgemeinbildend) + ',';
		result += '"berufsbildend" : ' + JSON.stringify(obj.berufsbildend) + ',';
		result += '"jahrgangVon" : ' + JSON.stringify(obj.jahrgangVon) + ',';
		result += '"jahrgangBis" : ' + JSON.stringify(obj.jahrgangBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulgliederungGueltigerAbschluss>) : string {
		let result = '{';
		if (obj.allgemeinbildend !== undefined) {
			result += '"allgemeinbildend" : ' + JSON.stringify(obj.allgemeinbildend) + ',';
		}
		if (obj.berufsbildend !== undefined) {
			result += '"berufsbildend" : ' + JSON.stringify(obj.berufsbildend) + ',';
		}
		if (obj.jahrgangVon !== undefined) {
			result += '"jahrgangVon" : ' + JSON.stringify(obj.jahrgangVon) + ',';
		}
		if (obj.jahrgangBis !== undefined) {
			result += '"jahrgangBis" : ' + JSON.stringify(obj.jahrgangBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_SchulgliederungGueltigerAbschluss(obj : unknown) : SchulgliederungGueltigerAbschluss {
	return obj as SchulgliederungGueltigerAbschluss;
}
