import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBlockungKursLehrer extends JavaObject {

	/**
	 * Die ID des Lehrers 
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Lehrers. 
	 */
	public kuerzel : String = "";

	/**
	 * Der Vorname des Lehrers. 
	 */
	public vorname : String = "";

	/**
	 * Der Nachname des Lehrers. 
	 */
	public nachname : String = "";

	/**
	 * Eine Reihenfolge für die Lehrer, z.B. zur Unterscheidung des eigentlichen Kurslehrer (z.B. 1) und einer Zusatzkraft (z.B. 2) 
	 */
	public reihenfolge : number = 1;

	/**
	 * Die Wochenstunden, welche die Lehrkraft in dem Kurs unterrichtet (Default: Wochenstunden des Kurses) 
	 */
	public wochenstunden : number = 3;

	/**
	 * Gibt an, ob es sich um eine externe Lehrkraft handelt (z.B. bei einem Kooperationskurs an einer anderen Schule) 
	 */
	public istExtern : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungKursLehrer'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungKursLehrer {
		const obj = JSON.parse(json);
		const result = new GostBlockungKursLehrer();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = String(obj.vorname);
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = String(obj.nachname);
		if (typeof obj.reihenfolge === "undefined")
			 throw new Error('invalid json format, missing attribute reihenfolge');
		result.reihenfolge = obj.reihenfolge;
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (typeof obj.istExtern === "undefined")
			 throw new Error('invalid json format, missing attribute istExtern');
		result.istExtern = obj.istExtern;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungKursLehrer) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"istExtern" : ' + obj.istExtern + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungKursLehrer>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		}
		if (typeof obj.reihenfolge !== "undefined") {
			result += '"reihenfolge" : ' + obj.reihenfolge + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (typeof obj.istExtern !== "undefined") {
			result += '"istExtern" : ' + obj.istExtern + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungKursLehrer(obj : unknown) : GostBlockungKursLehrer {
	return obj as GostBlockungKursLehrer;
}
