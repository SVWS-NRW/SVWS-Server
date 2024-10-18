import { JavaObject } from '../../../java/lang/JavaObject';
import { PersonTyp } from '../../../core/types/schule/PersonTyp';
import { Class } from '../../../java/lang/Class';

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
	 * Gibt an, ob die Einwilligungsart in der Anwendung sichtbar sein soll oder nicht.
	 */
	public sichtbar : boolean = true;

	/**
	 * Der Schlüssel der Einwilligungsart.
	 */
	public schluessel : string = "";

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung : number = 1;

	/**
	 * Eine ausführliche Beschreibung der Einwilligungsart.
	 */
	public beschreibung : string | null = "";

	/**
	 * Gibt an, für welche Personengruppe die Einwilligungsart relevant ist.
	 */
	public personTyp : number = PersonTyp.SCHUELER.id;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Einwilligungsart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Einwilligungsart'].includes(name);
	}

	public static class = new Class<Einwilligungsart>('de.svws_nrw.core.data.schule.Einwilligungsart');

	public static transpilerFromJSON(json : string): Einwilligungsart {
		const obj = JSON.parse(json) as Partial<Einwilligungsart>;
		const result = new Einwilligungsart();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sichtbar === undefined)
			throw new Error('invalid json format, missing attribute sichtbar');
		result.sichtbar = obj.sichtbar;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.personTyp === undefined)
			throw new Error('invalid json format, missing attribute personTyp');
		result.personTyp = obj.personTyp;
		return result;
	}

	public static transpilerToJSON(obj : Einwilligungsart) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sichtbar" : ' + obj.sichtbar.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"personTyp" : ' + obj.personTyp.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Einwilligungsart>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.sichtbar !== undefined) {
			result += '"sichtbar" : ' + obj.sichtbar.toString() + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.personTyp !== undefined) {
			result += '"personTyp" : ' + obj.personTyp.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Einwilligungsart(obj : unknown) : Einwilligungsart {
	return obj as Einwilligungsart;
}
