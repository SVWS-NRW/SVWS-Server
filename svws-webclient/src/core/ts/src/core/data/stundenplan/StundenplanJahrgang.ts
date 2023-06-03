import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanJahrgang extends JavaObject {

	/**
	 * Die ID des Jahrgangs.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Jahrgangs.
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung des Jahrgangs.
	 */
	public bezeichnung : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanJahrgang'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanJahrgang {
		const obj = JSON.parse(json);
		const result = new StundenplanJahrgang();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanJahrgang) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanJahrgang>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanJahrgang(obj : unknown) : StundenplanJahrgang {
	return obj as StundenplanJahrgang;
}
