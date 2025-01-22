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
	 * Erstellt einen SchulgliederungGueltigerAbschluss mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt ein neue Kombination von Berufsbildendem und Allgemeinbildendem Abschluss, welche
	 * für die angegebenen Jahrgänge gpltig sind.
	 *
	 * @param berufsbildend      der Bezeichner des berufsbildenden Abschlusses
	 * @param allgemeinbildend   der Bezeichner des allgemeinbildenden Abschlusses
	 * @param jahrgangVon        der Jahrgang, ab dem die Kombination möglich ist
	 * @param jahrgangBis        der Jahrgang, bis zu welchem die Kombination möglich ist
	 */
	public constructor(berufsbildend : string, allgemeinbildend : string, jahrgangVon : string, jahrgangBis : string);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : string, __param1? : string, __param2? : string, __param3? : string) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string")) && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && (typeof __param3 === "string"))) {
			const berufsbildend : string = __param0;
			const allgemeinbildend : string = __param1;
			const jahrgangVon : string = __param2;
			const jahrgangBis : string = __param3;
			this.allgemeinbildend = allgemeinbildend;
			this.berufsbildend = berufsbildend;
			this.jahrgangVon = jahrgangVon;
			this.jahrgangBis = jahrgangBis;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die Bezeichner der Abschlüsse und Jahrgänge getrennt durch Bindestriche als String aus.
	 *
	 * @return der String
	 */
	public toString() : string | null {
		return this.berufsbildend + "-" + this.allgemeinbildend + "-" + this.jahrgangVon + "-" + this.jahrgangBis;
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
