import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KatalogEntlassgrund extends JavaObject {

	/**
	 * Die ID des Entlassgrundes
	 */
	public id : number = 0;

	/**
	 * Die Bezeichnung des Entlassgrundes
	 */
	public bezeichnung : string = "";

	/**
	 * Die Sortierreihenfolge des Entlassgrund-Eintrags
	 */
	public sortierung : number = 32000;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung änderbar sein soll.
	 */
	public istAenderbar : boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.KatalogEntlassgrund';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kataloge.KatalogEntlassgrund'].includes(name);
	}

	public static class = new Class<KatalogEntlassgrund>('de.svws_nrw.core.data.kataloge.KatalogEntlassgrund');

	public static transpilerFromJSON(json : string): KatalogEntlassgrund {
		const obj = JSON.parse(json) as Partial<KatalogEntlassgrund>;
		const result = new KatalogEntlassgrund();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
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

	public static transpilerToJSON(obj : KatalogEntlassgrund) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KatalogEntlassgrund>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
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

export function cast_de_svws_nrw_core_data_kataloge_KatalogEntlassgrund(obj : unknown) : KatalogEntlassgrund {
	return obj as KatalogEntlassgrund;
}
