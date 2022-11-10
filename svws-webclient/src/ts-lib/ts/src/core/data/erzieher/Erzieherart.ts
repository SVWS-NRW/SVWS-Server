import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Erzieherart extends JavaObject {

	public id : Number | null = null;

	public bezeichnung : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.erzieher.Erzieherart'].includes(name);
	}

	public static transpilerFromJSON(json : string): Erzieherart {
		const obj = JSON.parse(json);
		const result = new Erzieherart();
		result.id = typeof obj.id === "undefined" ? null : obj.id === null ? null : Number(obj.id);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		return result;
	}

	public static transpilerToJSON(obj : Erzieherart) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Erzieherart>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_erzieher_Erzieherart(obj : unknown) : Erzieherart {
	return obj as Erzieherart;
}
