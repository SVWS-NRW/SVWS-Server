import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BenutzerConfigElement extends JavaObject {

	/**
	 * Der Schlüssel des Konfigurationselements
	 */
	public key: string = "";

	/**
	 * Der Wert des Konfigurationselements.
	 */
	public value: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor();

	/**
	 * Erzeugt ein neues Konfigurationselement
	 *
	 * @param key     der Schlüssel des Konfigurationselements
	 * @param value   der Wert des Konfigurationselements
	 */
	public constructor(key: string, value: string);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0?: string, __param1?: string) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && ((__param1 !== undefined) && (typeof __param1 === "string"))) {
			const key: string = __param0;
			const value: string = __param1;
			this.key = key;
			this.value = value;
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerConfigElement';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerConfigElement'].includes(name);
	}

	public static readonly class = new Class<BenutzerConfigElement>('de.svws_nrw.core.data.benutzer.BenutzerConfigElement');

	public static transpilerFromJSON(json: string): BenutzerConfigElement {
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

	public static transpilerToJSON(obj: BenutzerConfigElement): string {
		let result = '{';
		result += '"key" : ' + JSON.stringify(obj.key) + ',';
		result += '"value" : ' + JSON.stringify(obj.value) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<BenutzerConfigElement>): string {
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

export function cast_de_svws_nrw_core_data_benutzer_BenutzerConfigElement(obj: unknown): BenutzerConfigElement {
	return obj as BenutzerConfigElement;
}
