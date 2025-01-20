import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMServerConfigElement extends JavaObject {

	/**
	 * Der Schlüssel des Konfigurationselements
	 */
	public key : string = "";

	/**
	 * Der Wert des Konfigurationselements.
	 */
	public value : string = "";

	/**
	 * Der Typ des Konfigurationselements.
	 */
	public type : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMServerConfigElement';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMServerConfigElement'].includes(name);
	}

	public static class = new Class<ENMServerConfigElement>('de.svws_nrw.core.data.enm.ENMServerConfigElement');

	public static transpilerFromJSON(json : string): ENMServerConfigElement {
		const obj = JSON.parse(json) as Partial<ENMServerConfigElement>;
		const result = new ENMServerConfigElement();
		if (obj.key === undefined)
			throw new Error('invalid json format, missing attribute key');
		result.key = obj.key;
		if (obj.value === undefined)
			throw new Error('invalid json format, missing attribute value');
		result.value = obj.value;
		if (obj.type === undefined)
			throw new Error('invalid json format, missing attribute type');
		result.type = obj.type;
		return result;
	}

	public static transpilerToJSON(obj : ENMServerConfigElement) : string {
		let result = '{';
		result += '"key" : ' + JSON.stringify(obj.key) + ',';
		result += '"value" : ' + JSON.stringify(obj.value) + ',';
		result += '"type" : ' + JSON.stringify(obj.type) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMServerConfigElement>) : string {
		let result = '{';
		if (obj.key !== undefined) {
			result += '"key" : ' + JSON.stringify(obj.key) + ',';
		}
		if (obj.value !== undefined) {
			result += '"value" : ' + JSON.stringify(obj.value) + ',';
		}
		if (obj.type !== undefined) {
			result += '"type" : ' + JSON.stringify(obj.type) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMServerConfigElement(obj : unknown) : ENMServerConfigElement {
	return obj as ENMServerConfigElement;
}
