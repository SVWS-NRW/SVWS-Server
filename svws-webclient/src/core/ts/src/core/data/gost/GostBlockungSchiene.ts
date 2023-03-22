import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBlockungSchiene extends JavaObject {

	/**
	 * Die ID der Schiene
	 */
	public id : number = -1;

	/**
	 * Die Nummer der Schiene bei der Blockung (zur Sortierung)
	 */
	public nummer : number = 1;

	/**
	 * Bezeichnung der Schiene (z.B. LK Schiene 1)
	 */
	public bezeichnung : string = "Neue Schiene";

	/**
	 * Die Anzahl der Wochenstunden, welche der Schiene zugeordnet sind
	 */
	public wochenstunden : number = 3;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungSchiene'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungSchiene {
		const obj = JSON.parse(json);
		const result = new GostBlockungSchiene();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.nummer === "undefined")
			 throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungSchiene) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"nummer" : ' + obj.nummer + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungSchiene>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.nummer !== "undefined") {
			result += '"nummer" : ' + obj.nummer + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene(obj : unknown) : GostBlockungSchiene {
	return obj as GostBlockungSchiene;
}
