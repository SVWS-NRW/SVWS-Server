import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerLernabschnittNachpruefung extends JavaObject {

	/**
	 * Der Grund für die Versetzung (V - Versetzung, A - Abschluss, B - berufsqualifizierend) 
	 */
	public grund : string = "V";

	/**
	 * Die ID des Faches, auf welches sich die Leistungsdaten beziehen. 
	 */
	public fachID : number = -1;

	/**
	 * Das Kürzel der Note in der Nachprüfung. 
	 */
	public note : string | null = null;

	/**
	 * Das Datum der Nachprüfung. 
	 */
	public datum : string | null = null;


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
		result.grund = obj.grund;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.note = typeof obj.note === "undefined" ? null : obj.note === null ? null : obj.note;
		result.datum = typeof obj.datum === "undefined" ? null : obj.datum === null ? null : obj.datum;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittNachpruefung) : string {
		let result = '{';
		result += '"grund" : ' + '"' + obj.grund! + '"' + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note + '"') + ',';
		result += '"datum" : ' + ((!obj.datum) ? 'null' : '"' + obj.datum + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittNachpruefung>) : string {
		let result = '{';
		if (typeof obj.grund !== "undefined") {
			result += '"grund" : ' + '"' + obj.grund + '"' + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note + '"') + ',';
		}
		if (typeof obj.datum !== "undefined") {
			result += '"datum" : ' + ((!obj.datum) ? 'null' : '"' + obj.datum + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerLernabschnittNachpruefung(obj : unknown) : SchuelerLernabschnittNachpruefung {
	return obj as SchuelerLernabschnittNachpruefung;
}
