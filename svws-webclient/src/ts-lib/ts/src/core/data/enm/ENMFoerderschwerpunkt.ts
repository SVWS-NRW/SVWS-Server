import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMFoerderschwerpunkt extends JavaObject {

	public kuerzel : String | null = null;

	public beschreibung : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMFoerderschwerpunkt'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMFoerderschwerpunkt {
		const obj = JSON.parse(json);
		const result = new ENMFoerderschwerpunkt();
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel;
		result.beschreibung = typeof obj.beschreibung === "undefined" ? null : obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : ENMFoerderschwerpunkt) : string {
		let result = '{';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMFoerderschwerpunkt>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMFoerderschwerpunkt(obj : unknown) : ENMFoerderschwerpunkt {
	return obj as ENMFoerderschwerpunkt;
}
