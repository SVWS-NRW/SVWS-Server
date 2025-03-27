import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Lernplattform extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Lernplattform.
	 */
	public bezeichnung : string = "";

	/**
	 * Suffix für den Benutzernamen bei den Lehrern.
	 */
	public benutzernameSuffixLehrer : string | null = "";

	/**
	 * Suffix für den Benutzernamen bei den Erziehern.
	 */
	public benutzernameSuffixErzieher : string | null = "";

	/**
	 * Suffix für den Benutzernamen bei den Schülern.
	 */
	public benutzernameSuffixSchueler : string | null = "";

	/**
	 * Json-Objekt mit den Konfigurationseinstellungen der Accounterstellung für die Lernplattform.
	 */
	public konfiguration : string | null = "";

	/**
	 * Gibt an, für welche Personengruppe die Lernplattform relevant ist.
	 */
	public anzahlEinwilligungen : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Lernplattform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Lernplattform'].includes(name);
	}

	public static class = new Class<Lernplattform>('de.svws_nrw.core.data.schule.Lernplattform');

	public static transpilerFromJSON(json : string): Lernplattform {
		const obj = JSON.parse(json) as Partial<Lernplattform>;
		const result = new Lernplattform();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.benutzernameSuffixLehrer = (obj.benutzernameSuffixLehrer === undefined) ? null : obj.benutzernameSuffixLehrer === null ? null : obj.benutzernameSuffixLehrer;
		result.benutzernameSuffixErzieher = (obj.benutzernameSuffixErzieher === undefined) ? null : obj.benutzernameSuffixErzieher === null ? null : obj.benutzernameSuffixErzieher;
		result.benutzernameSuffixSchueler = (obj.benutzernameSuffixSchueler === undefined) ? null : obj.benutzernameSuffixSchueler === null ? null : obj.benutzernameSuffixSchueler;
		result.konfiguration = (obj.konfiguration === undefined) ? null : obj.konfiguration === null ? null : obj.konfiguration;
		if (obj.anzahlEinwilligungen === undefined)
			throw new Error('invalid json format, missing attribute anzahlEinwilligungen');
		result.anzahlEinwilligungen = obj.anzahlEinwilligungen;
		return result;
	}

	public static transpilerToJSON(obj : Lernplattform) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"benutzernameSuffixLehrer" : ' + ((obj.benutzernameSuffixLehrer === null) ? 'null' : JSON.stringify(obj.benutzernameSuffixLehrer)) + ',';
		result += '"benutzernameSuffixErzieher" : ' + ((obj.benutzernameSuffixErzieher === null) ? 'null' : JSON.stringify(obj.benutzernameSuffixErzieher)) + ',';
		result += '"benutzernameSuffixSchueler" : ' + ((obj.benutzernameSuffixSchueler === null) ? 'null' : JSON.stringify(obj.benutzernameSuffixSchueler)) + ',';
		result += '"konfiguration" : ' + ((obj.konfiguration === null) ? 'null' : JSON.stringify(obj.konfiguration)) + ',';
		result += '"anzahlEinwilligungen" : ' + obj.anzahlEinwilligungen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Lernplattform>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.benutzernameSuffixLehrer !== undefined) {
			result += '"benutzernameSuffixLehrer" : ' + ((obj.benutzernameSuffixLehrer === null) ? 'null' : JSON.stringify(obj.benutzernameSuffixLehrer)) + ',';
		}
		if (obj.benutzernameSuffixErzieher !== undefined) {
			result += '"benutzernameSuffixErzieher" : ' + ((obj.benutzernameSuffixErzieher === null) ? 'null' : JSON.stringify(obj.benutzernameSuffixErzieher)) + ',';
		}
		if (obj.benutzernameSuffixSchueler !== undefined) {
			result += '"benutzernameSuffixSchueler" : ' + ((obj.benutzernameSuffixSchueler === null) ? 'null' : JSON.stringify(obj.benutzernameSuffixSchueler)) + ',';
		}
		if (obj.konfiguration !== undefined) {
			result += '"konfiguration" : ' + ((obj.konfiguration === null) ? 'null' : JSON.stringify(obj.konfiguration)) + ',';
		}
		if (obj.anzahlEinwilligungen !== undefined) {
			result += '"anzahlEinwilligungen" : ' + obj.anzahlEinwilligungen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Lernplattform(obj : unknown) : Lernplattform {
	return obj as Lernplattform;
}
