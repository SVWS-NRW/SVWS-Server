import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerSchulbesuchSchule extends JavaObject {

	/**
	 * Die Schulnummer der Schule. 
	 */
	public schulnummer : string = "";

	/**
	 * Die ID des Bildungsganges/Schulgliederung an der Schule. 
	 */
	public schulgliederung : string | null = null;

	/**
	 * Die ID des Grundes für die Entlassung von der Schule. 
	 */
	public entlassgrundID : number | null = null;

	/**
	 * Die ID des Abschlusses, welcher an der Schule erworben wurde. 
	 */
	public abschlussartID : string | null = null;

	/**
	 * Die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht). 
	 */
	public organisationsFormID : string | null = null;

	/**
	 * Das Datum, ab dem die Schule besucht wurde. 
	 */
	public datumVon : string | null = null;

	/**
	 * Das Datum, bis wann die Schule besucht wurde. 
	 */
	public datumBis : string | null = null;

	/**
	 * Der Jahrgang, ab dem die Schule besucht wurde. 
	 */
	public jahrgangVon : string | null = null;

	/**
	 * Der Jahrgang, bis zu dem die Schule besucht wurde. 
	 */
	public jahrgangBis : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerSchulbesuchSchule'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerSchulbesuchSchule {
		const obj = JSON.parse(json);
		const result = new SchuelerSchulbesuchSchule();
		if (typeof obj.schulnummer === "undefined")
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.schulgliederung = typeof obj.schulgliederung === "undefined" ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.entlassgrundID = typeof obj.entlassgrundID === "undefined" ? null : obj.entlassgrundID === null ? null : obj.entlassgrundID;
		result.abschlussartID = typeof obj.abschlussartID === "undefined" ? null : obj.abschlussartID === null ? null : obj.abschlussartID;
		result.organisationsFormID = typeof obj.organisationsFormID === "undefined" ? null : obj.organisationsFormID === null ? null : obj.organisationsFormID;
		result.datumVon = typeof obj.datumVon === "undefined" ? null : obj.datumVon === null ? null : obj.datumVon;
		result.datumBis = typeof obj.datumBis === "undefined" ? null : obj.datumBis === null ? null : obj.datumBis;
		result.jahrgangVon = typeof obj.jahrgangVon === "undefined" ? null : obj.jahrgangVon === null ? null : obj.jahrgangVon;
		result.jahrgangBis = typeof obj.jahrgangBis === "undefined" ? null : obj.jahrgangBis === null ? null : obj.jahrgangBis;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchSchule) : string {
		let result = '{';
		result += '"schulnummer" : ' + '"' + obj.schulnummer! + '"' + ',';
		result += '"schulgliederung" : ' + ((!obj.schulgliederung) ? 'null' : '"' + obj.schulgliederung + '"') + ',';
		result += '"entlassgrundID" : ' + ((!obj.entlassgrundID) ? 'null' : obj.entlassgrundID) + ',';
		result += '"abschlussartID" : ' + ((!obj.abschlussartID) ? 'null' : '"' + obj.abschlussartID + '"') + ',';
		result += '"organisationsFormID" : ' + ((!obj.organisationsFormID) ? 'null' : '"' + obj.organisationsFormID + '"') + ',';
		result += '"datumVon" : ' + ((!obj.datumVon) ? 'null' : '"' + obj.datumVon + '"') + ',';
		result += '"datumBis" : ' + ((!obj.datumBis) ? 'null' : '"' + obj.datumBis + '"') + ',';
		result += '"jahrgangVon" : ' + ((!obj.jahrgangVon) ? 'null' : '"' + obj.jahrgangVon + '"') + ',';
		result += '"jahrgangBis" : ' + ((!obj.jahrgangBis) ? 'null' : '"' + obj.jahrgangBis + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerSchulbesuchSchule>) : string {
		let result = '{';
		if (typeof obj.schulnummer !== "undefined") {
			result += '"schulnummer" : ' + '"' + obj.schulnummer + '"' + ',';
		}
		if (typeof obj.schulgliederung !== "undefined") {
			result += '"schulgliederung" : ' + ((!obj.schulgliederung) ? 'null' : '"' + obj.schulgliederung + '"') + ',';
		}
		if (typeof obj.entlassgrundID !== "undefined") {
			result += '"entlassgrundID" : ' + ((!obj.entlassgrundID) ? 'null' : obj.entlassgrundID) + ',';
		}
		if (typeof obj.abschlussartID !== "undefined") {
			result += '"abschlussartID" : ' + ((!obj.abschlussartID) ? 'null' : '"' + obj.abschlussartID + '"') + ',';
		}
		if (typeof obj.organisationsFormID !== "undefined") {
			result += '"organisationsFormID" : ' + ((!obj.organisationsFormID) ? 'null' : '"' + obj.organisationsFormID + '"') + ',';
		}
		if (typeof obj.datumVon !== "undefined") {
			result += '"datumVon" : ' + ((!obj.datumVon) ? 'null' : '"' + obj.datumVon + '"') + ',';
		}
		if (typeof obj.datumBis !== "undefined") {
			result += '"datumBis" : ' + ((!obj.datumBis) ? 'null' : '"' + obj.datumBis + '"') + ',';
		}
		if (typeof obj.jahrgangVon !== "undefined") {
			result += '"jahrgangVon" : ' + ((!obj.jahrgangVon) ? 'null' : '"' + obj.jahrgangVon + '"') + ',';
		}
		if (typeof obj.jahrgangBis !== "undefined") {
			result += '"jahrgangBis" : ' + ((!obj.jahrgangBis) ? 'null' : '"' + obj.jahrgangBis + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchSchule(obj : unknown) : SchuelerSchulbesuchSchule {
	return obj as SchuelerSchulbesuchSchule;
}
