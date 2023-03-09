import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BetriebListeEintrag extends JavaObject {

	/**
	 * ID der weiteren Adresse (Betriebe) 
	 */
	public id : number = 0;

	/**
	 * Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart 
	 */
	public adressArt : number | null = null;

	/**
	 * Name1 des Betriebs 
	 */
	public name1 : string | null = null;

	/**
	 * Straßenname des Betriebsdatensatz 
	 */
	public strassenname : string | null = null;

	/**
	 * Hausnummer wenn getrennt gespeichert 
	 */
	public hausnr : string | null = null;

	/**
	 * Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden 
	 */
	public hausnrzusatz : string | null = null;

	/**
	 * OrtID des Betriebs 
	 */
	public ort_id : number | null = null;

	/**
	 * Brache des Betriebs 
	 */
	public branche : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.betrieb.BetriebListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BetriebListeEintrag {
		const obj = JSON.parse(json);
		const result = new BetriebListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.adressArt = typeof obj.adressArt === "undefined" ? null : obj.adressArt === null ? null : obj.adressArt;
		result.name1 = typeof obj.name1 === "undefined" ? null : obj.name1 === null ? null : obj.name1;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnr = typeof obj.hausnr === "undefined" ? null : obj.hausnr === null ? null : obj.hausnr;
		result.hausnrzusatz = typeof obj.hausnrzusatz === "undefined" ? null : obj.hausnrzusatz === null ? null : obj.hausnrzusatz;
		result.ort_id = typeof obj.ort_id === "undefined" ? null : obj.ort_id === null ? null : obj.ort_id;
		result.branche = typeof obj.branche === "undefined" ? null : obj.branche === null ? null : obj.branche;
		return result;
	}

	public static transpilerToJSON(obj : BetriebListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt) + ',';
		result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1 + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr + '"') + ',';
		result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz + '"') + ',';
		result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id) + ',';
		result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BetriebListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.adressArt !== "undefined") {
			result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt) + ',';
		}
		if (typeof obj.name1 !== "undefined") {
			result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1 + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		}
		if (typeof obj.hausnr !== "undefined") {
			result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr + '"') + ',';
		}
		if (typeof obj.hausnrzusatz !== "undefined") {
			result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz + '"') + ',';
		}
		if (typeof obj.ort_id !== "undefined") {
			result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id) + ',';
		}
		if (typeof obj.branche !== "undefined") {
			result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_betrieb_BetriebListeEintrag(obj : unknown) : BetriebListeEintrag {
	return obj as BetriebListeEintrag;
}
