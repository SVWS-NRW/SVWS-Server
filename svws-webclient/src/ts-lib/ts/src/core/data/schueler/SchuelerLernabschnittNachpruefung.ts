import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerLernabschnittNachpruefung extends JavaObject {

	public grund : String = "V";

	public fachID : number = -1;

	public note : String | null = null;

	public datum : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerLernabschnittNachpruefung'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerLernabschnittNachpruefung {
		const obj = JSON.parse(json);
		const result = new SchuelerLernabschnittNachpruefung();
		if (typeof obj.grund === "undefined")
			 throw new Error('invalid json format, missing attribute grund');
		result.grund = String(obj.grund);
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.note = typeof obj.note === "undefined" ? null : obj.note === null ? null : String(obj.note);
		result.datum = typeof obj.datum === "undefined" ? null : obj.datum === null ? null : String(obj.datum);
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittNachpruefung) : string {
		let result = '{';
		result += '"grund" : ' + '"' + obj.grund.valueOf() + '"' + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		result += '"datum" : ' + ((!obj.datum) ? 'null' : '"' + obj.datum.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittNachpruefung>) : string {
		let result = '{';
		if (typeof obj.grund !== "undefined") {
			result += '"grund" : ' + '"' + obj.grund.valueOf() + '"' + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		}
		if (typeof obj.datum !== "undefined") {
			result += '"datum" : ' + ((!obj.datum) ? 'null' : '"' + obj.datum.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerLernabschnittNachpruefung(obj : unknown) : SchuelerLernabschnittNachpruefung {
	return obj as SchuelerLernabschnittNachpruefung;
}
