import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Telefonnummer extends JavaObject {

	/**
	 *  die Art der Telefonnummer
	 */
	public type : string = "";

	/**
	 *  die Telefon-, Fax- oder Pagernummer
	 */
	public number : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.adressbuch.Telefonnummer';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.adressbuch.Telefonnummer'].includes(name);
	}

	public static class = new Class<Telefonnummer>('de.svws_nrw.core.data.adressbuch.Telefonnummer');

	public static transpilerFromJSON(json : string): Telefonnummer {
		const obj = JSON.parse(json) as Partial<Telefonnummer>;
		const result = new Telefonnummer();
		if (obj.type === undefined)
			throw new Error('invalid json format, missing attribute type');
		result.type = obj.type;
		if (obj.number === undefined)
			throw new Error('invalid json format, missing attribute number');
		result.number = obj.number;
		return result;
	}

	public static transpilerToJSON(obj : Telefonnummer) : string {
		let result = '{';
		result += '"type" : ' + JSON.stringify(obj.type) + ',';
		result += '"number" : ' + JSON.stringify(obj.number) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Telefonnummer>) : string {
		let result = '{';
		if (obj.type !== undefined) {
			result += '"type" : ' + JSON.stringify(obj.type) + ',';
		}
		if (obj.number !== undefined) {
			result += '"number" : ' + JSON.stringify(obj.number) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_adressbuch_Telefonnummer(obj : unknown) : Telefonnummer {
	return obj as Telefonnummer;
}
