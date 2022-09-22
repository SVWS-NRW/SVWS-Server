import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BetriebListeEintrag extends JavaObject {

	public id : Number | null = null;

	public adressArt : Number | null = null;

	public name1 : String | null = null;

	public strassenname : String | null = null;

	public hausnr : String | null = null;

	public hausnrzusatz : String | null = null;

	public ort_id : Number | null = null;

	public branche : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.betrieb.BetriebListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BetriebListeEintrag {
		const obj = JSON.parse(json);
		const result = new BetriebListeEintrag();
		result.id = typeof obj.id === "undefined" ? null : obj.id;
		result.adressArt = typeof obj.adressArt === "undefined" ? null : obj.adressArt;
		result.name1 = typeof obj.name1 === "undefined" ? null : obj.name1;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname;
		result.hausnr = typeof obj.hausnr === "undefined" ? null : obj.hausnr;
		result.hausnrzusatz = typeof obj.hausnrzusatz === "undefined" ? null : obj.hausnrzusatz;
		result.ort_id = typeof obj.ort_id === "undefined" ? null : obj.ort_id;
		result.branche = typeof obj.branche === "undefined" ? null : obj.branche;
		return result;
	}

	public static transpilerToJSON(obj : BetriebListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt.valueOf()) + ',';
		result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1.valueOf() + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr.valueOf() + '"') + ',';
		result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz.valueOf() + '"') + ',';
		result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id.valueOf()) + ',';
		result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BetriebListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		}
		if (typeof obj.adressArt !== "undefined") {
			result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt.valueOf()) + ',';
		}
		if (typeof obj.name1 !== "undefined") {
			result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1.valueOf() + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnr !== "undefined") {
			result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnrzusatz !== "undefined") {
			result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz.valueOf() + '"') + ',';
		}
		if (typeof obj.ort_id !== "undefined") {
			result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id.valueOf()) + ',';
		}
		if (typeof obj.branche !== "undefined") {
			result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_betrieb_BetriebListeEintrag(obj : unknown) : BetriebListeEintrag {
	return obj as BetriebListeEintrag;
}
