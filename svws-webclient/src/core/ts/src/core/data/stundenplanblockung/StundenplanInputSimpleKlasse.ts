import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanInputSimpleKlasse extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Klasse. Beispielsweise '7b' oder 'Q1'.
	 */
	public kuerzel : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanInputSimpleKlasse'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimpleKlasse {
		const obj = JSON.parse(json);
		const result = new StundenplanInputSimpleKlasse();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleKlasse) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleKlasse>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleKlasse(obj : unknown) : StundenplanInputSimpleKlasse {
	return obj as StundenplanInputSimpleKlasse;
}
