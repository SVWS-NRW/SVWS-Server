import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerblockungInputFachwahl extends JavaObject {

	public id : number = 0;

	public fach : number = 0;

	public kursart : number = 0;

	public representation : String = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.SchuelerblockungInputFachwahl'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungInputFachwahl {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungInputFachwahl();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.fach === "undefined")
			 throw new Error('invalid json format, missing attribute fach');
		result.fach = obj.fach;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.representation === "undefined")
			 throw new Error('invalid json format, missing attribute representation');
		result.representation = String(obj.representation);
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungInputFachwahl) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"fach" : ' + obj.fach + ',';
		result += '"kursart" : ' + obj.kursart + ',';
		result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungInputFachwahl>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.fach !== "undefined") {
			result += '"fach" : ' + obj.fach + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + obj.kursart + ',';
		}
		if (typeof obj.representation !== "undefined") {
			result += '"representation" : ' + '"' + obj.representation.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputFachwahl(obj : unknown) : SchuelerblockungInputFachwahl {
	return obj as SchuelerblockungInputFachwahl;
}
