import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class OrtsteilKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Die ID des zugehörigen Ortes.
	 */
	public ort_id : number | null = null;

	/**
	 * Der Name des Ortsteils.
	 */
	public ortsteil : string | null = null;

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag'].includes(name);
	}

	public static class = new Class<OrtsteilKatalogEintrag>('de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag');

	public static transpilerFromJSON(json : string): OrtsteilKatalogEintrag {
		const obj = JSON.parse(json) as Partial<OrtsteilKatalogEintrag>;
		const result = new OrtsteilKatalogEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.ort_id = (obj.ort_id === undefined) ? null : obj.ort_id === null ? null : obj.ort_id;
		result.ortsteil = (obj.ortsteil === undefined) ? null : obj.ortsteil === null ? null : obj.ortsteil;
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

	public static transpilerToJSON(obj : OrtsteilKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"ort_id" : ' + ((obj.ort_id === null) ? 'null' : obj.ort_id.toString()) + ',';
		result += '"ortsteil" : ' + ((obj.ortsteil === null) ? 'null' : JSON.stringify(obj.ortsteil)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<OrtsteilKatalogEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.ort_id !== undefined) {
			result += '"ort_id" : ' + ((obj.ort_id === null) ? 'null' : obj.ort_id.toString()) + ',';
		}
		if (obj.ortsteil !== undefined) {
			result += '"ortsteil" : ' + ((obj.ortsteil === null) ? 'null' : JSON.stringify(obj.ortsteil)) + ',';
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

export function cast_de_svws_nrw_core_data_kataloge_OrtsteilKatalogEintrag(obj : unknown) : OrtsteilKatalogEintrag {
	return obj as OrtsteilKatalogEintrag;
}
