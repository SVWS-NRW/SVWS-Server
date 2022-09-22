import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBlockungsergebnisSchuelerzuordnung extends JavaObject {

	public id : number = -1;

	public name : String = "Mustermann, Max";

	public hatKollisionen : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisSchuelerzuordnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisSchuelerzuordnung {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisSchuelerzuordnung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.hatKollisionen === "undefined")
			 throw new Error('invalid json format, missing attribute hatKollisionen');
		result.hatKollisionen = obj.hatKollisionen;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisSchuelerzuordnung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		result += '"hatKollisionen" : ' + obj.hatKollisionen + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisSchuelerzuordnung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name.valueOf() + '"' + ',';
		}
		if (typeof obj.hatKollisionen !== "undefined") {
			result += '"hatKollisionen" : ' + obj.hatKollisionen + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisSchuelerzuordnung(obj : unknown) : GostBlockungsergebnisSchuelerzuordnung {
	return obj as GostBlockungsergebnisSchuelerzuordnung;
}
