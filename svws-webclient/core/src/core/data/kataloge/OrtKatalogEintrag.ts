import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class OrtKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Die Postleitzahl.
	 */
	public plz : string | null = null;

	/**
	 * Der Name des Ortes.
	 */
	public ortsname : string | null = null;

	/**
	 * Der Name des Kreises.
	 */
	public kreis : string | null = null;

	/**
	 * Das Kürzel des Bundeslandes.
	 */
	public kuerzelBundesland : string | null = null;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht.
	 */
	public istAenderbar : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.OrtKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kataloge.OrtKatalogEintrag'].includes(name);
	}

	public static class = new Class<OrtKatalogEintrag>('de.svws_nrw.core.data.kataloge.OrtKatalogEintrag');

	public static transpilerFromJSON(json : string): OrtKatalogEintrag {
		const obj = JSON.parse(json) as Partial<OrtKatalogEintrag>;
		const result = new OrtKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ortsname = (obj.ortsname === undefined) ? null : obj.ortsname === null ? null : obj.ortsname;
		result.kreis = (obj.kreis === undefined) ? null : obj.kreis === null ? null : obj.kreis;
		result.kuerzelBundesland = (obj.kuerzelBundesland === undefined) ? null : obj.kuerzelBundesland === null ? null : obj.kuerzelBundesland;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.istAenderbar === undefined)
			throw new Error('invalid json format, missing attribute istAenderbar');
		result.istAenderbar = obj.istAenderbar;
		return result;
	}

	public static transpilerToJSON(obj : OrtKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ortsname" : ' + ((!obj.ortsname) ? 'null' : JSON.stringify(obj.ortsname)) + ',';
		result += '"kreis" : ' + ((!obj.kreis) ? 'null' : JSON.stringify(obj.kreis)) + ',';
		result += '"kuerzelBundesland" : ' + ((!obj.kuerzelBundesland) ? 'null' : JSON.stringify(obj.kuerzelBundesland)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<OrtKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ortsname !== undefined) {
			result += '"ortsname" : ' + ((!obj.ortsname) ? 'null' : JSON.stringify(obj.ortsname)) + ',';
		}
		if (obj.kreis !== undefined) {
			result += '"kreis" : ' + ((!obj.kreis) ? 'null' : JSON.stringify(obj.kreis)) + ',';
		}
		if (obj.kuerzelBundesland !== undefined) {
			result += '"kuerzelBundesland" : ' + ((!obj.kuerzelBundesland) ? 'null' : JSON.stringify(obj.kuerzelBundesland)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.istAenderbar !== undefined) {
			result += '"istAenderbar" : ' + obj.istAenderbar.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kataloge_OrtKatalogEintrag(obj : unknown) : OrtKatalogEintrag {
	return obj as OrtKatalogEintrag;
}
