import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanblockungRaum extends JavaObject {

	/**
	 * Die Datenbank-ID des Raumes.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Raumes. Beispielsweise 'E21'.
	 */
	public kuerzel : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungRaum';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungRaum'].includes(name);
	}

	public static class = new Class<StundenplanblockungRaum>('de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungRaum');

	public static transpilerFromJSON(json : string): StundenplanblockungRaum {
		const obj = JSON.parse(json) as Partial<StundenplanblockungRaum>;
		const result = new StundenplanblockungRaum();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungRaum) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungRaum>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungRaum(obj : unknown) : StundenplanblockungRaum {
	return obj as StundenplanblockungRaum;
}
