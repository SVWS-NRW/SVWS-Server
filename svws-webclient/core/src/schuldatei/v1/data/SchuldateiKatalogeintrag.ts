import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuldateiKatalogeintrag extends JavaObject {

	/**
	 * Der Katalog, welchem der Eintrag zugeordnet ist
	 */
	public katalog : string = "";

	/**
	 * Der Schlüssel des Katalog-Eintrags
	 */
	public schluessel : string = "";

	/**
	 * Der Wert des Katalog-Eintrags
	 */
	public wert : string = "";

	/**
	 * Die Bezeichnung
	 */
	public bezeichnung : string = "";

	/**
	 * Gibt an, ab wann der Eintrag gültig ist
	 */
	public gueltigab : string | null = null;

	/**
	 * Gibt an, bis wann der Eintrag gültig ist
	 */
	public gueltigbis : string | null = null;

	/**
	 * Das Änderungsdatum der letzten Änderung des Eintrags an
	 */
	public geaendertam : string | null = null;


	/**
	 * Erstellt einen neuen Eintrag zu einer Adresse einer Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
		// empty block
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiKatalogeintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiKatalogeintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiKatalogeintrag {
		const obj = JSON.parse(json);
		const result = new SchuldateiKatalogeintrag();
		if (typeof obj.katalog === "undefined")
			 throw new Error('invalid json format, missing attribute katalog');
		result.katalog = obj.katalog;
		if (typeof obj.schluessel === "undefined")
			 throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (typeof obj.wert === "undefined")
			 throw new Error('invalid json format, missing attribute wert');
		result.wert = obj.wert;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.gueltigab = typeof obj.gueltigab === "undefined" ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = typeof obj.gueltigbis === "undefined" ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = typeof obj.geaendertam === "undefined" ? null : obj.geaendertam === null ? null : obj.geaendertam;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiKatalogeintrag) : string {
		let result = '{';
		result += '"katalog" : ' + JSON.stringify(obj.katalog!) + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		result += '"wert" : ' + JSON.stringify(obj.wert!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiKatalogeintrag>) : string {
		let result = '{';
		if (typeof obj.katalog !== "undefined") {
			result += '"katalog" : ' + JSON.stringify(obj.katalog!) + ',';
		}
		if (typeof obj.schluessel !== "undefined") {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		}
		if (typeof obj.wert !== "undefined") {
			result += '"wert" : ' + JSON.stringify(obj.wert!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.gueltigab !== "undefined") {
			result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (typeof obj.gueltigbis !== "undefined") {
			result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (typeof obj.geaendertam !== "undefined") {
			result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiKatalogeintrag(obj : unknown) : SchuldateiKatalogeintrag {
	return obj as SchuldateiKatalogeintrag;
}
