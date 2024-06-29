import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';

export class SchuldateiKatalogeintrag extends SchuldateiEintrag {

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
	 * Erstellt einen neuen Eintrag zu einer Adresse einer Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiKatalogeintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiEintrag', 'de.svws_nrw.schulen.v1.data.SchuldateiKatalogeintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiKatalogeintrag {
		const obj = JSON.parse(json);
		const result = new SchuldateiKatalogeintrag();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if (obj.katalog === undefined)
			 throw new Error('invalid json format, missing attribute katalog');
		result.katalog = obj.katalog;
		if (obj.schluessel === undefined)
			 throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.wert === undefined)
			 throw new Error('invalid json format, missing attribute wert');
		result.wert = obj.wert;
		if (obj.bezeichnung === undefined)
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiKatalogeintrag) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"katalog" : ' + JSON.stringify(obj.katalog!) + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		result += '"wert" : ' + JSON.stringify(obj.wert!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiKatalogeintrag>) : string {
		let result = '{';
		if (obj.gueltigab !== undefined) {
			result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (obj.gueltigbis !== undefined) {
			result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (obj.geaendertam !== undefined) {
			result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		if (obj.katalog !== undefined) {
			result += '"katalog" : ' + JSON.stringify(obj.katalog!) + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		}
		if (obj.wert !== undefined) {
			result += '"wert" : ' + JSON.stringify(obj.wert!) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiKatalogeintrag(obj : unknown) : SchuldateiKatalogeintrag {
	return obj as SchuldateiKatalogeintrag;
}
