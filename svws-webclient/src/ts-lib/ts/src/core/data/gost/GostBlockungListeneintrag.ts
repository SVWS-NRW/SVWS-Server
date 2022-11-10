import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBlockungListeneintrag extends JavaObject {

	public id : number = -1;

	public name : String = "Neue Blockung";

	public gostHalbjahr : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungListeneintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungListeneintrag {
		const obj = JSON.parse(json);
		const result = new GostBlockungListeneintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = String(obj.name);
		if (typeof obj.gostHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungListeneintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungListeneintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.gostHalbjahr !== "undefined") {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungListeneintrag(obj : unknown) : GostBlockungListeneintrag {
	return obj as GostBlockungListeneintrag;
}
