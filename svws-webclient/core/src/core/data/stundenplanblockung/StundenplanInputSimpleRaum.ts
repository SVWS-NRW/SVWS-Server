import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanInputSimpleRaum extends JavaObject {

	/**
	 * Die ID des Raumes.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Raumes. Beispielsweise 'SpH1' oder 'BK05'.
	 */
	public kuerzel : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleRaum';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleRaum'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimpleRaum {
		const obj = JSON.parse(json);
		const result = new StundenplanInputSimpleRaum();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleRaum) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleRaum>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimpleRaum(obj : unknown) : StundenplanInputSimpleRaum {
	return obj as StundenplanInputSimpleRaum;
}
