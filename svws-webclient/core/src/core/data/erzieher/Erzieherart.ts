import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Erzieherart extends JavaObject {

	/**
	 * ID der Erzieherart
	 */
	public id : number = -1;

	/**
	 * Bezeichnung der Erzieherart
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
	 * Exportbezeichnung der Erzieherart
	 */
	public exportBez : string | null = "";

	/**
	 * Gibt an wie vielen Erziehungsberechtigten die entsprechende Erzieherart zugeordnet sind.
	 */
	public anzahlErziehungsberechtigte : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.erzieher.Erzieherart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.erzieher.Erzieherart'].includes(name);
	}

	public static class = new Class<Erzieherart>('de.svws_nrw.core.data.erzieher.Erzieherart');

	public static transpilerFromJSON(json : string): Erzieherart {
		const obj = JSON.parse(json) as Partial<Erzieherart>;
		const result = new Erzieherart();
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
		result.exportBez = (obj.exportBez === undefined) ? null : obj.exportBez === null ? null : obj.exportBez;
		if (obj.anzahlErziehungsberechtigte === undefined)
			throw new Error('invalid json format, missing attribute anzahlErziehungsberechtigte');
		result.anzahlErziehungsberechtigte = obj.anzahlErziehungsberechtigte;
		return result;
	}

	public static transpilerToJSON(obj : Erzieherart) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"exportBez" : ' + ((obj.exportBez === null) ? 'null' : JSON.stringify(obj.exportBez)) + ',';
		result += '"anzahlErziehungsberechtigte" : ' + obj.anzahlErziehungsberechtigte.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Erzieherart>) : string {
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
		if (obj.exportBez !== undefined) {
			result += '"exportBez" : ' + ((obj.exportBez === null) ? 'null' : JSON.stringify(obj.exportBez)) + ',';
		}
		if (obj.anzahlErziehungsberechtigte !== undefined) {
			result += '"anzahlErziehungsberechtigte" : ' + obj.anzahlErziehungsberechtigte.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_erzieher_Erzieherart(obj : unknown) : Erzieherart {
	return obj as Erzieherart;
}
