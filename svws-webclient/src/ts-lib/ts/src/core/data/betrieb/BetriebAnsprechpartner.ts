import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BetriebAnsprechpartner extends JavaObject {

	public id : Number | null = null;

	public betrieb_id : Number | null = null;

	public name : String | null = null;

	public vorname : String | null = null;

	public anrede : String | null = null;

	public telefon : String | null = null;

	public email : String | null = null;

	public abteilung : String | null = null;

	public titel : String | null = null;

	public GU_ID : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.betrieb.BetriebAnsprechpartner'].includes(name);
	}

	public static transpilerFromJSON(json : string): BetriebAnsprechpartner {
		const obj = JSON.parse(json);
		const result = new BetriebAnsprechpartner();
		result.id = typeof obj.id === "undefined" ? null : obj.id;
		result.betrieb_id = typeof obj.betrieb_id === "undefined" ? null : obj.betrieb_id;
		result.name = typeof obj.name === "undefined" ? null : obj.name;
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname;
		result.anrede = typeof obj.anrede === "undefined" ? null : obj.anrede;
		result.telefon = typeof obj.telefon === "undefined" ? null : obj.telefon;
		result.email = typeof obj.email === "undefined" ? null : obj.email;
		result.abteilung = typeof obj.abteilung === "undefined" ? null : obj.abteilung;
		result.titel = typeof obj.titel === "undefined" ? null : obj.titel;
		result.GU_ID = typeof obj.GU_ID === "undefined" ? null : obj.GU_ID;
		return result;
	}

	public static transpilerToJSON(obj : BetriebAnsprechpartner) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		result += '"betrieb_id" : ' + ((!obj.betrieb_id) ? 'null' : obj.betrieb_id.valueOf()) + ',';
		result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name.valueOf() + '"') + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede.valueOf() + '"') + ',';
		result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon.valueOf() + '"') + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		result += '"abteilung" : ' + ((!obj.abteilung) ? 'null' : '"' + obj.abteilung.valueOf() + '"') + ',';
		result += '"titel" : ' + ((!obj.titel) ? 'null' : '"' + obj.titel.valueOf() + '"') + ',';
		result += '"GU_ID" : ' + ((!obj.GU_ID) ? 'null' : '"' + obj.GU_ID.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BetriebAnsprechpartner>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		}
		if (typeof obj.betrieb_id !== "undefined") {
			result += '"betrieb_id" : ' + ((!obj.betrieb_id) ? 'null' : obj.betrieb_id.valueOf()) + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + ((!obj.name) ? 'null' : '"' + obj.name.valueOf() + '"') + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		}
		if (typeof obj.anrede !== "undefined") {
			result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede.valueOf() + '"') + ',';
		}
		if (typeof obj.telefon !== "undefined") {
			result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon.valueOf() + '"') + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		}
		if (typeof obj.abteilung !== "undefined") {
			result += '"abteilung" : ' + ((!obj.abteilung) ? 'null' : '"' + obj.abteilung.valueOf() + '"') + ',';
		}
		if (typeof obj.titel !== "undefined") {
			result += '"titel" : ' + ((!obj.titel) ? 'null' : '"' + obj.titel.valueOf() + '"') + ',';
		}
		if (typeof obj.GU_ID !== "undefined") {
			result += '"GU_ID" : ' + ((!obj.GU_ID) ? 'null' : '"' + obj.GU_ID.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_betrieb_BetriebAnsprechpartner(obj : unknown) : BetriebAnsprechpartner {
	return obj as BetriebAnsprechpartner;
}
