import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Beschaeftigungsart extends JavaObject {

	/**
	 * Die ID der Beschäftigungsart
	 */
	public id : number = 0;

	/**
	 * Die Bezeichnung der Beschäftigungsart
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die Position in der Sortierreihenfolge der Beschäftigungsarten
	 */
	public sortierung : number = 0;

	/**
	 * Die Sichtbarkeit der Beschäftigungsart
	 */
	public istSichtbar : boolean = false;

	/**
	 * Die Änderbarkeit der Beschäftigungsart
	 */
	public istAenderbar : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.betrieb.Beschaeftigungsart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.betrieb.Beschaeftigungsart'].includes(name);
	}

	public static class = new Class<Beschaeftigungsart>('de.svws_nrw.core.data.betrieb.Beschaeftigungsart');

	public static transpilerFromJSON(json : string): Beschaeftigungsart {
		const obj = JSON.parse(json) as Partial<Beschaeftigungsart>;
		const result = new Beschaeftigungsart();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
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

	public static transpilerToJSON(obj : Beschaeftigungsart) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Beschaeftigungsart>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
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

export function cast_de_svws_nrw_core_data_betrieb_Beschaeftigungsart(obj : unknown) : Beschaeftigungsart {
	return obj as Beschaeftigungsart;
}
