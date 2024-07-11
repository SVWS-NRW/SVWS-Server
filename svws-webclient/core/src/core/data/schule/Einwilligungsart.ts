import { JavaObject } from '../../../java/lang/JavaObject';
import { PersonTyp } from '../../../core/types/schule/PersonTyp';

export class Einwilligungsart extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Einwilligungsart.
	 */
	public bezeichnung : string = "";

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung : number = 1;

	/**
	 * Gibt an, für welche Personengruppe die Einwilligungsart relevant ist.
	 */
	public personTyp : number = PersonTyp.SCHUELER.id;

	/**
	 * Der Schlüssel der Einwilligungsart.
	 */
	public schluessel : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Einwilligungsart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Einwilligungsart'].includes(name);
	}

	public static transpilerFromJSON(json : string): Einwilligungsart {
		const obj = JSON.parse(json);
		const result = new Einwilligungsart();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sortierung === undefined)
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.personTyp === undefined)
			 throw new Error('invalid json format, missing attribute personTyp');
		result.personTyp = obj.personTyp;
		if (obj.schluessel === undefined)
			 throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		return result;
	}

	public static transpilerToJSON(obj : Einwilligungsart) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"personTyp" : ' + obj.personTyp + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Einwilligungsart>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (obj.personTyp !== undefined) {
			result += '"personTyp" : ' + obj.personTyp + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Einwilligungsart(obj : unknown) : Einwilligungsart {
	return obj as Einwilligungsart;
}
