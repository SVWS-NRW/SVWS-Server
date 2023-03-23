import { JavaObject } from '../../../java/lang/JavaObject';

export class ReligionEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung bzw. der Name der Religion.
	 */
	public text : string | null = "";

	/**
	 * Die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint.
	 */
	public textZeugnis : string | null = "";

	/**
	 * Das Kürzel des Eintrages für die Statistik.
	 */
	public kuerzel : string | null = "";

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung : number = 1;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = true;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht.
	 */
	public istAenderbar : boolean = true;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.ReligionEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): ReligionEintrag {
		const obj = JSON.parse(json);
		const result = new ReligionEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.text = typeof obj.text === "undefined" ? null : obj.text === null ? null : obj.text;
		result.textZeugnis = typeof obj.textZeugnis === "undefined" ? null : obj.textZeugnis === null ? null : obj.textZeugnis;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (typeof obj.istAenderbar === "undefined")
			 throw new Error('invalid json format, missing attribute istAenderbar');
		result.istAenderbar = obj.istAenderbar;
		return result;
	}

	public static transpilerToJSON(obj : ReligionEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text + '"') + ',';
		result += '"textZeugnis" : ' + ((!obj.textZeugnis) ? 'null' : '"' + obj.textZeugnis + '"') + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ReligionEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text + '"') + ',';
		}
		if (typeof obj.textZeugnis !== "undefined") {
			result += '"textZeugnis" : ' + ((!obj.textZeugnis) ? 'null' : '"' + obj.textZeugnis + '"') + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		if (typeof obj.istAenderbar !== "undefined") {
			result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_ReligionEintrag(obj : unknown) : ReligionEintrag {
	return obj as ReligionEintrag;
}
