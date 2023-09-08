import { JavaObject } from '../../../java/lang/JavaObject';
import { RGBFarbe } from '../../../core/data/RGBFarbe';

export class StundenplanFach extends JavaObject {

	/**
	 * Die ID des Faches.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Faches.
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung des Faches.
	 */
	public bezeichnung : string = "";

	/**
	 * Eine Nummer, welche die Sortierreihenfolge bei den Fächern angibt.
	 */
	public sortierung : number = 32000;

	/**
	 * Die Farbe, die zur Darstellung des Faches genutzt werden soll - sofern vom Standard abgewichen werden soll.
	 */
	public farbe : RGBFarbe | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanFach {
		const obj = JSON.parse(json);
		const result = new StundenplanFach();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.farbe = ((typeof obj.farbe === "undefined") || (obj.farbe === null)) ? null : RGBFarbe.transpilerFromJSON(JSON.stringify(obj.farbe));
		return result;
	}

	public static transpilerToJSON(obj : StundenplanFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"farbe" : ' + ((!obj.farbe) ? 'null' : RGBFarbe.transpilerToJSON(obj.farbe)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanFach>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.farbe !== "undefined") {
			result += '"farbe" : ' + ((!obj.farbe) ? 'null' : RGBFarbe.transpilerToJSON(obj.farbe)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanFach(obj : unknown) : StundenplanFach {
	return obj as StundenplanFach;
}
