import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BenutzerConfigElement extends JavaObject {

	/**
	 * Der Schlüssel des Konfigurationselements 
	 */
	public key : string = "";

	/**
	 * Der Wert des Konfigurationselements. 
	 */
	public value : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzerConfigElement'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerConfigElement {
		const obj = JSON.parse(json);
		const result = new BenutzerConfigElement();
		if (typeof obj.key === "undefined")
			 throw new Error('invalid json format, missing attribute key');
		result.key = obj.key;
		if (typeof obj.value === "undefined")
			 throw new Error('invalid json format, missing attribute value');
		result.value = obj.value;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerConfigElement) : string {
		let result = '{';
		result += '"key" : ' + '"' + obj.key! + '"' + ',';
		result += '"value" : ' + '"' + obj.value! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerConfigElement>) : string {
		let result = '{';
		if (typeof obj.key !== "undefined") {
			result += '"key" : ' + '"' + obj.key + '"' + ',';
		}
		if (typeof obj.value !== "undefined") {
			result += '"value" : ' + '"' + obj.value + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzerConfigElement(obj : unknown) : BenutzerConfigElement {
	return obj as BenutzerConfigElement;
}
