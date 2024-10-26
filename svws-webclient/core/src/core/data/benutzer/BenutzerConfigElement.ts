import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BenutzerConfigElement extends JavaObject {

	/**
	 * Der Schlüssel des Konfigurationselements
	 */
	public key : string = "";

	/**
	 * Der Wert des Konfigurationselements.
	 */
	public value : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerConfigElement';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerConfigElement'].includes(name);
	}

	public static class = new Class<BenutzerConfigElement>('de.svws_nrw.core.data.benutzer.BenutzerConfigElement');

	public static transpilerFromJSON(json : string): BenutzerConfigElement {
		const obj = JSON.parse(json) as Partial<BenutzerConfigElement>;
		const result = new BenutzerConfigElement();
		if (obj.key === undefined)
			throw new Error('invalid json format, missing attribute key');
		result.key = obj.key;
		if (obj.value === undefined)
			throw new Error('invalid json format, missing attribute value');
		result.value = obj.value;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerConfigElement) : string {
		let result = '{';
		result += '"key" : ' + JSON.stringify(obj.key) + ',';
		result += '"value" : ' + JSON.stringify(obj.value) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerConfigElement>) : string {
		let result = '{';
		if (obj.key !== undefined) {
			result += '"key" : ' + JSON.stringify(obj.key) + ',';
		}
		if (obj.value !== undefined) {
			result += '"value" : ' + JSON.stringify(obj.value) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzerConfigElement(obj : unknown) : BenutzerConfigElement {
	return obj as BenutzerConfigElement;
}
