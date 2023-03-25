import { JavaObject } from '../../../java/lang/JavaObject';

export class AdressbuchEintrag extends JavaObject {

	/**
	 * ID des AdressbuchEintrags
	 */
	public id : string = "";

	/**
	 * ID des Adressbuchs
	 */
	public adressbuchId : string = "";

	/**
	 *  URI der VCard des Kontakts
	 */
	public uri : string = "";

	/**
	 *  Versionskennzeichen des Kontaks
	 */
	public version : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.AdressbuchEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): AdressbuchEintrag {
		const obj = JSON.parse(json);
		const result = new AdressbuchEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.adressbuchId === "undefined")
			 throw new Error('invalid json format, missing attribute adressbuchId');
		result.adressbuchId = obj.adressbuchId;
		if (typeof obj.uri === "undefined")
			 throw new Error('invalid json format, missing attribute uri');
		result.uri = obj.uri;
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		return result;
	}

	public static transpilerToJSON(obj : AdressbuchEintrag) : string {
		let result = '{';
		result += '"id" : ' + '"' + obj.id! + '"' + ',';
		result += '"adressbuchId" : ' + '"' + obj.adressbuchId! + '"' + ',';
		result += '"uri" : ' + '"' + obj.uri! + '"' + ',';
		result += '"version" : ' + '"' + obj.version! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AdressbuchEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + '"' + obj.id + '"' + ',';
		}
		if (typeof obj.adressbuchId !== "undefined") {
			result += '"adressbuchId" : ' + '"' + obj.adressbuchId + '"' + ',';
		}
		if (typeof obj.uri !== "undefined") {
			result += '"uri" : ' + '"' + obj.uri + '"' + ',';
		}
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + '"' + obj.version + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_adressbuch_AdressbuchEintrag(obj : unknown) : AdressbuchEintrag {
	return obj as AdressbuchEintrag;
}
