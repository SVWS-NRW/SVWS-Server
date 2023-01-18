import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMFach extends JavaObject {

	/**
	 * Die ID des Faches in der SVWS-DB. 
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. D) 
	 */
	public kuerzel : String = "";

	/**
	 * Das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. D) 
	 */
	public kuerzelAnzeige : String = "";

	/**
	 * Die Reihenfolge des Faches bei der Sortierung der Fächer. (z.B. 37) 
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht. 
	 */
	public istFremdsprache : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMFach {
		const obj = JSON.parse(json);
		const result = new ENMFach();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.kuerzelAnzeige === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzelAnzeige');
		result.kuerzelAnzeige = String(obj.kuerzelAnzeige);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istFremdsprache === "undefined")
			 throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		return result;
	}

	public static transpilerToJSON(obj : ENMFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"kuerzelAnzeige" : ' + '"' + obj.kuerzelAnzeige.valueOf() + '"' + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMFach>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.kuerzelAnzeige !== "undefined") {
			result += '"kuerzelAnzeige" : ' + '"' + obj.kuerzelAnzeige.valueOf() + '"' + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istFremdsprache !== "undefined") {
			result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMFach(obj : unknown) : ENMFach {
	return obj as ENMFach;
}
