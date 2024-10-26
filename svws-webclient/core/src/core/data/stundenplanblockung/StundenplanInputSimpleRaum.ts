import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanInputSimpleRaum extends JavaObject {

	/**
	 * Die ID des Raumes.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Raumes. Beispielsweise 'SpH1' oder 'BK05'.
	 */
	public kuerzel : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleRaum';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleRaum'].includes(name);
	}

	public static class = new Class<StundenplanInputSimpleRaum>('de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleRaum');

	public static transpilerFromJSON(json : string): StundenplanInputSimpleRaum {
		const obj = JSON.parse(json) as Partial<StundenplanInputSimpleRaum>;
		const result = new StundenplanInputSimpleRaum();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleRaum) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleRaum>) : string {
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

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimpleRaum(obj : unknown) : StundenplanInputSimpleRaum {
	return obj as StundenplanInputSimpleRaum;
}
