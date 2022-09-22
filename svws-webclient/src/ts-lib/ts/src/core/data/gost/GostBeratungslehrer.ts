import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBeratungslehrer extends JavaObject {

	public id : number = 0;

	public kuerzel : String | null = null;

	public nachname : String | null = null;

	public vorname : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBeratungslehrer'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBeratungslehrer {
		const obj = JSON.parse(json);
		const result = new GostBeratungslehrer();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel;
		result.nachname = typeof obj.nachname === "undefined" ? null : obj.nachname;
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname;
		return result;
	}

	public static transpilerToJSON(obj : GostBeratungslehrer) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname.valueOf() + '"') + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBeratungslehrer>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname.valueOf() + '"') + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBeratungslehrer(obj : unknown) : GostBeratungslehrer {
	return obj as GostBeratungslehrer;
}
