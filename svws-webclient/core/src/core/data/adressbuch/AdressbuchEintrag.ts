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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.adressbuch.AdressbuchEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.AdressbuchEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): AdressbuchEintrag {
		const obj = JSON.parse(json);
		const result = new AdressbuchEintrag();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.adressbuchId === undefined)
			 throw new Error('invalid json format, missing attribute adressbuchId');
		result.adressbuchId = obj.adressbuchId;
		if (obj.uri === undefined)
			 throw new Error('invalid json format, missing attribute uri');
		result.uri = obj.uri;
		if (obj.version === undefined)
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		return result;
	}

	public static transpilerToJSON(obj : AdressbuchEintrag) : string {
		let result = '{';
		result += '"id" : ' + JSON.stringify(obj.id!) + ',';
		result += '"adressbuchId" : ' + JSON.stringify(obj.adressbuchId!) + ',';
		result += '"uri" : ' + JSON.stringify(obj.uri!) + ',';
		result += '"version" : ' + JSON.stringify(obj.version!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AdressbuchEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + JSON.stringify(obj.id!) + ',';
		}
		if (obj.adressbuchId !== undefined) {
			result += '"adressbuchId" : ' + JSON.stringify(obj.adressbuchId!) + ',';
		}
		if (obj.uri !== undefined) {
			result += '"uri" : ' + JSON.stringify(obj.uri!) + ',';
		}
		if (obj.version !== undefined) {
			result += '"version" : ' + JSON.stringify(obj.version!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_adressbuch_AdressbuchEintrag(obj : unknown) : AdressbuchEintrag {
	return obj as AdressbuchEintrag;
}
