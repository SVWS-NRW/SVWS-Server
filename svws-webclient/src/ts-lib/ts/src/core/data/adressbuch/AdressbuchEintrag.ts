import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class AdressbuchEintrag extends JavaObject {

	public id : String = "";

	public adressbuchId : String = "";

	public uri : String = "";

	public version : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): AdressbuchEintrag {
		const obj = JSON.parse(json);
		const result = new AdressbuchEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = String(obj.id);
		if (typeof obj.adressbuchId === "undefined")
			 throw new Error('invalid json format, missing attribute adressbuchId');
		result.adressbuchId = String(obj.adressbuchId);
		if (typeof obj.uri === "undefined")
			 throw new Error('invalid json format, missing attribute uri');
		result.uri = String(obj.uri);
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = String(obj.version);
		return result;
	}

	public static transpilerToJSON(obj : AdressbuchEintrag) : string {
		let result = '{';
		result += '"id" : ' + '"' + obj.id.valueOf() + '"' + ',';
		result += '"adressbuchId" : ' + '"' + obj.adressbuchId.valueOf() + '"' + ',';
		result += '"uri" : ' + '"' + obj.uri.valueOf() + '"' + ',';
		result += '"version" : ' + '"' + obj.version.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AdressbuchEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + '"' + obj.id.valueOf() + '"' + ',';
		}
		if (typeof obj.adressbuchId !== "undefined") {
			result += '"adressbuchId" : ' + '"' + obj.adressbuchId.valueOf() + '"' + ',';
		}
		if (typeof obj.uri !== "undefined") {
			result += '"uri" : ' + '"' + obj.uri.valueOf() + '"' + ',';
		}
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + '"' + obj.version.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_adressbuch_AdressbuchEintrag(obj : unknown) : AdressbuchEintrag {
	return obj as AdressbuchEintrag;
}
