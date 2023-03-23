import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostKlausurenKalenderinformation extends JavaObject {

	/**
	 * Die ID der Kalenderinformation.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Kalenderinformation.
	 */
	public bezeichnung : string | null = "";

	/**
	 * Das Startdatum der Kalenderinformation.
	 */
	public startdatum : string | null = null;

	/**
	 * Die Startzeit der Kalenderinformation.
	 */
	public startzeit : string | null = null;

	/**
	 * Das Enddatum der Kalenderinformation.
	 */
	public enddatum : string | null = null;

	/**
	 * Die Endzeit der Kalenderinformation.
	 */
	public endzeit : string | null = null;

	/**
	 * Die textuelle Bemerkung zur Kalenderinformation, sofern vorhanden.
	 */
	public bemerkung : string | null = null;

	/**
	 * Die Information, ob es sich um einen Sperrtermin handelt.
	 */
	public istSperrtermin : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurenKalenderinformation'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenKalenderinformation {
		const obj = JSON.parse(json);
		const result = new GostKlausurenKalenderinformation();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.startdatum = typeof obj.startdatum === "undefined" ? null : obj.startdatum === null ? null : obj.startdatum;
		result.startzeit = typeof obj.startzeit === "undefined" ? null : obj.startzeit === null ? null : obj.startzeit;
		result.enddatum = typeof obj.enddatum === "undefined" ? null : obj.enddatum === null ? null : obj.enddatum;
		result.endzeit = typeof obj.endzeit === "undefined" ? null : obj.endzeit === null ? null : obj.endzeit;
		result.bemerkung = typeof obj.bemerkung === "undefined" ? null : obj.bemerkung === null ? null : obj.bemerkung;
		if (typeof obj.istSperrtermin === "undefined")
			 throw new Error('invalid json format, missing attribute istSperrtermin');
		result.istSperrtermin = obj.istSperrtermin;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenKalenderinformation) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		result += '"startdatum" : ' + ((!obj.startdatum) ? 'null' : '"' + obj.startdatum + '"') + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : '"' + obj.startzeit + '"') + ',';
		result += '"enddatum" : ' + ((!obj.enddatum) ? 'null' : '"' + obj.enddatum + '"') + ',';
		result += '"endzeit" : ' + ((!obj.endzeit) ? 'null' : '"' + obj.endzeit + '"') + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : '"' + obj.bemerkung + '"') + ',';
		result += '"istSperrtermin" : ' + obj.istSperrtermin + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenKalenderinformation>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		}
		if (typeof obj.startdatum !== "undefined") {
			result += '"startdatum" : ' + ((!obj.startdatum) ? 'null' : '"' + obj.startdatum + '"') + ',';
		}
		if (typeof obj.startzeit !== "undefined") {
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : '"' + obj.startzeit + '"') + ',';
		}
		if (typeof obj.enddatum !== "undefined") {
			result += '"enddatum" : ' + ((!obj.enddatum) ? 'null' : '"' + obj.enddatum + '"') + ',';
		}
		if (typeof obj.endzeit !== "undefined") {
			result += '"endzeit" : ' + ((!obj.endzeit) ? 'null' : '"' + obj.endzeit + '"') + ',';
		}
		if (typeof obj.bemerkung !== "undefined") {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : '"' + obj.bemerkung + '"') + ',';
		}
		if (typeof obj.istSperrtermin !== "undefined") {
			result += '"istSperrtermin" : ' + obj.istSperrtermin + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKlausurenKalenderinformation(obj : unknown) : GostKlausurenKalenderinformation {
	return obj as GostKlausurenKalenderinformation;
}
