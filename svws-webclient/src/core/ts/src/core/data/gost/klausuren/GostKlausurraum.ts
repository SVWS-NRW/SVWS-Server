import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostKlausurraum extends JavaObject {

	/**
	 * Die ID des Klausurraums.
	 */
	public id : number = -1;

	/**
	 * Die ID des Klausurtermins.
	 */
	public idTermin : number = -1;

	/**
	 * Die ID des Katalog_Raumes.
	 */
	public idKatalogRaum : number = -1;

	/**
	 * Die textuelle Bemerkung zum Klausurraum, sofern vorhanden.
	 */
	public bemerkung : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausuren.GostKlausurraum'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurraum {
		const obj = JSON.parse(json);
		const result = new GostKlausurraum();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idTermin === "undefined")
			 throw new Error('invalid json format, missing attribute idTermin');
		result.idTermin = obj.idTermin;
		if (typeof obj.idKatalogRaum === "undefined")
			 throw new Error('invalid json format, missing attribute idKatalogRaum');
		result.idKatalogRaum = obj.idKatalogRaum;
		result.bemerkung = typeof obj.bemerkung === "undefined" ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurraum) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idTermin" : ' + obj.idTermin + ',';
		result += '"idKatalogRaum" : ' + obj.idKatalogRaum + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : '"' + obj.bemerkung + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurraum>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idTermin !== "undefined") {
			result += '"idTermin" : ' + obj.idTermin + ',';
		}
		if (typeof obj.idKatalogRaum !== "undefined") {
			result += '"idKatalogRaum" : ' + obj.idKatalogRaum + ',';
		}
		if (typeof obj.bemerkung !== "undefined") {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : '"' + obj.bemerkung + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausuren_GostKlausurraum(obj : unknown) : GostKlausurraum {
	return obj as GostKlausurraum;
}
