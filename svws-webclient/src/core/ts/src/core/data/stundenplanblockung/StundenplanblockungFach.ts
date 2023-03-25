import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungFach extends JavaObject {

	/**
	 * Die Datenbank-ID des Faches.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Faches. Beispielsweise 'KU'.
	 */
	public kuerzel : string = "";

	/**
	 * Die Nummer, welche die Sortierung der Fächer angibt.
	 */
	public sortierung : number = 32000;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungFach {
		const obj = JSON.parse(json);
		const result = new StundenplanblockungFach();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungFach>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungFach(obj : unknown) : StundenplanblockungFach {
	return obj as StundenplanblockungFach;
}
