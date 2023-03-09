import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class StundenplanInputSimpleFach extends JavaObject {

	/**
	 * Die ID des Faches. 
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Faches. Beispielsweise 'D' oder 'E5'. 
	 */
	public kuerzel : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanInputSimpleFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimpleFach {
		const obj = JSON.parse(json);
		const result = new StundenplanInputSimpleFach();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleFach>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleFach(obj : unknown) : StundenplanInputSimpleFach {
	return obj as StundenplanInputSimpleFach;
}
