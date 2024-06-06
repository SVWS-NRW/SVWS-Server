import { JavaObject } from '../../../java/lang/JavaObject';

export class VermerkartEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Vermerkart.
	 */
	public bezeichnung : string = "";

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung : number = 1;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = true;

	/**
	 * Gibt an wie viele Vermerke dem entsprechenden Vermerkart-Eintrag zugeordnet sind (berechneter Wert).
	 */
	public anzahlVermerke : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.VermerkartEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.VermerkartEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): VermerkartEintrag {
		const obj = JSON.parse(json);
		const result = new VermerkartEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (typeof obj.anzahlVermerke === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlVermerke');
		result.anzahlVermerke = obj.anzahlVermerke;
		return result;
	}

	public static transpilerToJSON(obj : VermerkartEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"anzahlVermerke" : ' + obj.anzahlVermerke + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<VermerkartEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		if (typeof obj.anzahlVermerke !== "undefined") {
			result += '"anzahlVermerke" : ' + obj.anzahlVermerke + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_VermerkartEintrag(obj : unknown) : VermerkartEintrag {
	return obj as VermerkartEintrag;
}
