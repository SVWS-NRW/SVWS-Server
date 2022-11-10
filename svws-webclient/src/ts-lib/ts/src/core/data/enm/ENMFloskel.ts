import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMFloskel extends JavaObject {

	public kuerzel : String | null = null;

	public text : String | null = null;

	public fachID : Number | null = null;

	public niveau : Number | null = null;

	public jahrgangID : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMFloskel'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMFloskel {
		const obj = JSON.parse(json);
		const result = new ENMFloskel();
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : String(obj.kuerzel);
		result.text = typeof obj.text === "undefined" ? null : obj.text === null ? null : String(obj.text);
		result.fachID = typeof obj.fachID === "undefined" ? null : obj.fachID === null ? null : Number(obj.fachID);
		result.niveau = typeof obj.niveau === "undefined" ? null : obj.niveau === null ? null : Number(obj.niveau);
		result.jahrgangID = typeof obj.jahrgangID === "undefined" ? null : obj.jahrgangID === null ? null : Number(obj.jahrgangID);
		return result;
	}

	public static transpilerToJSON(obj : ENMFloskel) : string {
		let result = '{';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text.valueOf() + '"') + ',';
		result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID.valueOf()) + ',';
		result += '"niveau" : ' + ((!obj.niveau) ? 'null' : obj.niveau.valueOf()) + ',';
		result += '"jahrgangID" : ' + ((!obj.jahrgangID) ? 'null' : obj.jahrgangID.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMFloskel>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text.valueOf() + '"') + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID.valueOf()) + ',';
		}
		if (typeof obj.niveau !== "undefined") {
			result += '"niveau" : ' + ((!obj.niveau) ? 'null' : obj.niveau.valueOf()) + ',';
		}
		if (typeof obj.jahrgangID !== "undefined") {
			result += '"jahrgangID" : ' + ((!obj.jahrgangID) ? 'null' : obj.jahrgangID.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMFloskel(obj : unknown) : ENMFloskel {
	return obj as ENMFloskel;
}
