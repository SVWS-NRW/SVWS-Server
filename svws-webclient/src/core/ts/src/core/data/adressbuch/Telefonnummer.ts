import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Telefonnummer extends JavaObject {

	/**
	 *  die Art der Telefonnummer
	 */
	public type : string = "";

	/**
	 *  die Telefon-, Fax- oder Pagernummer
	 */
	public number : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.adressbuch.Telefonnummer'].includes(name);
	}

	public static transpilerFromJSON(json : string): Telefonnummer {
		const obj = JSON.parse(json);
		const result = new Telefonnummer();
		if (typeof obj.type === "undefined")
			 throw new Error('invalid json format, missing attribute type');
		result.type = obj.type;
		if (typeof obj.number === "undefined")
			 throw new Error('invalid json format, missing attribute number');
		result.number = obj.number;
		return result;
	}

	public static transpilerToJSON(obj : Telefonnummer) : string {
		let result = '{';
		result += '"type" : ' + '"' + obj.type! + '"' + ',';
		result += '"number" : ' + '"' + obj.number! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Telefonnummer>) : string {
		let result = '{';
		if (typeof obj.type !== "undefined") {
			result += '"type" : ' + '"' + obj.type + '"' + ',';
		}
		if (typeof obj.number !== "undefined") {
			result += '"number" : ' + '"' + obj.number + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_adressbuch_Telefonnummer(obj : unknown) : Telefonnummer {
	return obj as Telefonnummer;
}
