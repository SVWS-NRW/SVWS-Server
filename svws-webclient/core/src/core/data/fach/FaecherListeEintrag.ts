import { JavaObject } from '../../../java/lang/JavaObject';

export class FaecherListeEintrag extends JavaObject {

	/**
	 * Die ID des Faches.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Faches.
	 */
	public kuerzel : string = "";

	/**
	 * Das dem Fach zugeordnete Statistik-Kürzel.
	 */
	public kuerzelStatistik : string = "";

	/**
	 * Der Name / die Bezeichnung des Faches.
	 */
	public bezeichnung : string = "";

	/**
	 * Die Sortierreihenfolge des Fächerlisten-Eintrags.
	 */
	public sortierung : number = 32000;

	/**
	 * Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht.
	 */
	public istOberstufenFach : boolean = false;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.fach.FaecherListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.fach.FaecherListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): FaecherListeEintrag {
		const obj = JSON.parse(json);
		const result = new FaecherListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.kuerzelStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istOberstufenFach === "undefined")
			 throw new Error('invalid json format, missing attribute istOberstufenFach');
		result.istOberstufenFach = obj.istOberstufenFach;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		return result;
	}

	public static transpilerToJSON(obj : FaecherListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istOberstufenFach" : ' + obj.istOberstufenFach + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FaecherListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istOberstufenFach !== "undefined") {
			result += '"istOberstufenFach" : ' + obj.istOberstufenFach + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_fach_FaecherListeEintrag(obj : unknown) : FaecherListeEintrag {
	return obj as FaecherListeEintrag;
}
