import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerKatalogLeitungsfunktionEintrag extends JavaObject {

	public id : number = 0;

	public text : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.lehrer.LehrerKatalogLeitungsfunktionEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerKatalogLeitungsfunktionEintrag {
		const obj = JSON.parse(json);
		const result = new LehrerKatalogLeitungsfunktionEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.text === "undefined")
			 throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		return result;
	}

	public static transpilerToJSON(obj : LehrerKatalogLeitungsfunktionEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"text" : ' + '"' + obj.text.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerKatalogLeitungsfunktionEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + '"' + obj.text.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLeitungsfunktionEintrag(obj : unknown) : LehrerKatalogLeitungsfunktionEintrag {
	return obj as LehrerKatalogLeitungsfunktionEintrag;
}
