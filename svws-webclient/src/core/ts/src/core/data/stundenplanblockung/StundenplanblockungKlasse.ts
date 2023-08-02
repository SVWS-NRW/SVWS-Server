import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungKlasse extends JavaObject {

	/**
	 * Die Datenbank-ID der Klasse.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Lehrkraft. Beispielsweise '07c' oder 'Q1'.
	 */
	public kuerzel : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungKlasse'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungKlasse {
		const obj = JSON.parse(json);
		const result = new StundenplanblockungKlasse();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungKlasse) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungKlasse>) : string {
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

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungKlasse(obj : unknown) : StundenplanblockungKlasse {
	return obj as StundenplanblockungKlasse;
}
