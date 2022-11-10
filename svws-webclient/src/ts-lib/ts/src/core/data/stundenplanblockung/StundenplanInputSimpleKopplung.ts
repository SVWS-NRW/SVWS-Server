import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class StundenplanInputSimpleKopplung extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public stunden : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanInputSimpleKopplung'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimpleKopplung {
		const obj = JSON.parse(json);
		const result = new StundenplanInputSimpleKopplung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.stunden === "undefined")
			 throw new Error('invalid json format, missing attribute stunden');
		result.stunden = obj.stunden;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleKopplung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"stunden" : ' + obj.stunden + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleKopplung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.stunden !== "undefined") {
			result += '"stunden" : ' + obj.stunden + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleKopplung(obj : unknown) : StundenplanInputSimpleKopplung {
	return obj as StundenplanInputSimpleKopplung;
}
