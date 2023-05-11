import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanAufsichtsbereich extends JavaObject {

	/**
	 * Die ID des Aufsichtsbereichs.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Aufsichtsbereichs.
	 */
	public kuerzel : string = "";

	/**
	 * Die Beschreibung des Aufsichtsbereichs.
	 */
	public beschreibung : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanAufsichtsbereich {
		const obj = JSON.parse(json);
		const result = new StundenplanAufsichtsbereich();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.beschreibung === "undefined")
			 throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanAufsichtsbereich) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"beschreibung" : ' + '"' + obj.beschreibung! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanAufsichtsbereich>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + '"' + obj.beschreibung + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanAufsichtsbereich(obj : unknown) : StundenplanAufsichtsbereich {
	return obj as StundenplanAufsichtsbereich;
}
