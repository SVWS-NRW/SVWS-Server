import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostKlausurtermin extends JavaObject {

	/**
	 * Die ID des Klausurtermins.
	 */
	public id : number = -1;

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird.
	 */
	public abijahr : number = -1;

	/**
	 * Das Gost-Halbjahr, in dem die Klausurg geschrieben wird.
	 */
	public halbjahr : number = -1;

	/**
	 * Das Quartal, in welchem die Klausur gechrieben wird.
	 */
	public quartal : number = -1;

	/**
	 * Das Datum des Klausurtermins, falls schon gesetzt.
	 */
	public datum : string | null = null;

	/**
	 * Die Startzeit des Klausurtermins in Minuten seit 0 Uhr, falls schon gesetzt.
	 */
	public startzeit : number | null = null;

	/**
	 * Die Bezeichnung des Klausurtermins, falls schon gesetzt.
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die textuelle Bemerkung zum Termin, sofern vorhanden.
	 */
	public bemerkung : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurtermin {
		const obj = JSON.parse(json);
		const result = new GostKlausurtermin();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.abijahr === "undefined")
			 throw new Error('invalid json format, missing attribute abijahr');
		result.abijahr = obj.abijahr;
		if (typeof obj.halbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		if (typeof obj.quartal === "undefined")
			 throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		result.datum = typeof obj.datum === "undefined" ? null : obj.datum === null ? null : obj.datum;
		result.startzeit = typeof obj.startzeit === "undefined" ? null : obj.startzeit === null ? null : obj.startzeit;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.bemerkung = typeof obj.bemerkung === "undefined" ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurtermin) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"abijahr" : ' + obj.abijahr + ',';
		result += '"halbjahr" : ' + obj.halbjahr + ',';
		result += '"quartal" : ' + obj.quartal + ',';
		result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurtermin>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.abijahr !== "undefined") {
			result += '"abijahr" : ' + obj.abijahr + ',';
		}
		if (typeof obj.halbjahr !== "undefined") {
			result += '"halbjahr" : ' + obj.halbjahr + ',';
		}
		if (typeof obj.quartal !== "undefined") {
			result += '"quartal" : ' + obj.quartal + ',';
		}
		if (typeof obj.datum !== "undefined") {
			result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		}
		if (typeof obj.startzeit !== "undefined") {
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (typeof obj.bemerkung !== "undefined") {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurtermin(obj : unknown) : GostKlausurtermin {
	return obj as GostKlausurtermin;
}
