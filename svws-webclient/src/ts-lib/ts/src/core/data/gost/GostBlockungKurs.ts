import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBlockungKurs extends JavaObject {

	public id : number = -1;

	public fach_id : number = -1;

	public kursart : number = 0;

	public nummer : number = 0;

	public istKoopKurs : boolean = false;

	public suffix : String = "";

	public wochenstunden : number = 3;

	public anzahlSchienen : number = 1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungKurs {
		const obj = JSON.parse(json);
		const result = new GostBlockungKurs();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.fach_id === "undefined")
			 throw new Error('invalid json format, missing attribute fach_id');
		result.fach_id = obj.fach_id;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.nummer === "undefined")
			 throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		if (typeof obj.istKoopKurs === "undefined")
			 throw new Error('invalid json format, missing attribute istKoopKurs');
		result.istKoopKurs = obj.istKoopKurs;
		if (typeof obj.suffix === "undefined")
			 throw new Error('invalid json format, missing attribute suffix');
		result.suffix = String(obj.suffix);
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (typeof obj.anzahlSchienen === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSchienen');
		result.anzahlSchienen = obj.anzahlSchienen;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"fach_id" : ' + obj.fach_id + ',';
		result += '"kursart" : ' + obj.kursart + ',';
		result += '"nummer" : ' + obj.nummer + ',';
		result += '"istKoopKurs" : ' + obj.istKoopKurs + ',';
		result += '"suffix" : ' + '"' + obj.suffix.valueOf() + '"' + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"anzahlSchienen" : ' + obj.anzahlSchienen + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungKurs>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.fach_id !== "undefined") {
			result += '"fach_id" : ' + obj.fach_id + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + obj.kursart + ',';
		}
		if (typeof obj.nummer !== "undefined") {
			result += '"nummer" : ' + obj.nummer + ',';
		}
		if (typeof obj.istKoopKurs !== "undefined") {
			result += '"istKoopKurs" : ' + obj.istKoopKurs + ',';
		}
		if (typeof obj.suffix !== "undefined") {
			result += '"suffix" : ' + '"' + obj.suffix.valueOf() + '"' + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (typeof obj.anzahlSchienen !== "undefined") {
			result += '"anzahlSchienen" : ' + obj.anzahlSchienen + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs(obj : unknown) : GostBlockungKurs {
	return obj as GostBlockungKurs;
}
