import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMNote extends JavaObject {

	/**
	 * Die ID der Note.
	 */
	public id : number = 0;

	/**
	 * Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-), ggf. auch ein Kürzel für PseudoNoten
	 */
	public kuerzel : string | null = null;

	/**
	 * Die Notenpunkte, die dieser Note ggf. zugeordnet sind
	 */
	public notenpunkte : number | null = null;

	/**
	 * Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)
	 */
	public text : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMNote'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMNote {
		const obj = JSON.parse(json);
		const result = new ENMNote();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.notenpunkte = typeof obj.notenpunkte === "undefined" ? null : obj.notenpunkte === null ? null : obj.notenpunkte;
		result.text = typeof obj.text === "undefined" ? null : obj.text === null ? null : obj.text;
		return result;
	}

	public static transpilerToJSON(obj : ENMNote) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte) + ',';
		result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMNote>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		}
		if (typeof obj.notenpunkte !== "undefined") {
			result += '"notenpunkte" : ' + ((!obj.notenpunkte) ? 'null' : obj.notenpunkte) + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMNote(obj : unknown) : ENMNote {
	return obj as ENMNote;
}
