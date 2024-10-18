import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';
import { RGBFarbe } from '../../../asd/data/RGBFarbe';

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
	 * Das Statistik-Kürzel des Faches.
	 */
	public kuerzelStatistik : string = "";

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanFach'].includes(name);
	}

	public static class = new Class<StundenplanFach>('de.svws_nrw.core.data.stundenplan.StundenplanFach');

	public static transpilerFromJSON(json : string): StundenplanFach {
		const obj = JSON.parse(json) as Partial<StundenplanFach>;
		const result = new StundenplanFach();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.kuerzelStatistik === undefined)
			throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.farbe = ((obj.farbe === undefined) || (obj.farbe === null)) ? null : RGBFarbe.transpilerFromJSON(JSON.stringify(obj.farbe));
		return result;
	}

	public static transpilerToJSON(obj : StundenplanFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"farbe" : ' + ((!obj.farbe) ? 'null' : RGBFarbe.transpilerToJSON(obj.farbe)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanFach>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelStatistik !== undefined) {
			result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.farbe !== undefined) {
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
