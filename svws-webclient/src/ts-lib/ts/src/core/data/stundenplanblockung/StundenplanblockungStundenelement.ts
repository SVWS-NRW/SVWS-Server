import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class StundenplanblockungStundenelement extends JavaObject {

	public id : number = 0;

	public stunden : number = -1;

	public typ : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungStundenelement'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungStundenelement {
		const obj = JSON.parse(json);
		const result = new StundenplanblockungStundenelement();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.stunden === "undefined")
			 throw new Error('invalid json format, missing attribute stunden');
		result.stunden = obj.stunden;
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungStundenelement) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"stunden" : ' + obj.stunden + ',';
		result += '"typ" : ' + obj.typ + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungStundenelement>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.stunden !== "undefined") {
			result += '"stunden" : ' + obj.stunden + ',';
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + obj.typ + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanblockungStundenelement(obj : unknown) : StundenplanblockungStundenelement {
	return obj as StundenplanblockungStundenelement;
}
