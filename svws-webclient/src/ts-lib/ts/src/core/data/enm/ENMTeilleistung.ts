import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMTeilleistung extends JavaObject {

	public id : number = 0;

	public artID : number = 0;

	public datum : String | null = null;

	public bemerkung : String | null = null;

	public notenKuerzel : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMTeilleistung'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMTeilleistung {
		const obj = JSON.parse(json);
		const result = new ENMTeilleistung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.artID === "undefined")
			 throw new Error('invalid json format, missing attribute artID');
		result.artID = obj.artID;
		result.datum = typeof obj.datum === "undefined" ? null : obj.datum === null ? null : String(obj.datum);
		result.bemerkung = typeof obj.bemerkung === "undefined" ? null : obj.bemerkung === null ? null : String(obj.bemerkung);
		result.notenKuerzel = typeof obj.notenKuerzel === "undefined" ? null : obj.notenKuerzel === null ? null : String(obj.notenKuerzel);
		return result;
	}

	public static transpilerToJSON(obj : ENMTeilleistung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"artID" : ' + obj.artID + ',';
		result += '"datum" : ' + ((!obj.datum) ? 'null' : '"' + obj.datum.valueOf() + '"') + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : '"' + obj.bemerkung.valueOf() + '"') + ',';
		result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : '"' + obj.notenKuerzel.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMTeilleistung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.artID !== "undefined") {
			result += '"artID" : ' + obj.artID + ',';
		}
		if (typeof obj.datum !== "undefined") {
			result += '"datum" : ' + ((!obj.datum) ? 'null' : '"' + obj.datum.valueOf() + '"') + ',';
		}
		if (typeof obj.bemerkung !== "undefined") {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : '"' + obj.bemerkung.valueOf() + '"') + ',';
		}
		if (typeof obj.notenKuerzel !== "undefined") {
			result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : '"' + obj.notenKuerzel.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMTeilleistung(obj : unknown) : ENMTeilleistung {
	return obj as ENMTeilleistung;
}
