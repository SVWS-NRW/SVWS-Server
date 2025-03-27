import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchulEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Ein Kürzel, welches der Schule zugeordnet ist.
	 */
	public kuerzel : string | null = null;

	/**
	 * Eine Kurzbezeichnung für die Schule.
	 */
	public kurzbezeichnung : string | null = null;

	/**
	 * Die Statistik-Schulnummer der Schule
	 */
	public schulnummerStatistik : string | null = null;

	/**
	 * Der Name des Schule.
	 */
	public name : string = "";

	/**
	 * Die ID der Schulform.
	 */
	public idSchulform : number | null = null;

	/**
	 * Der Straßenname der Straße in der die Schule liegt.
	 */
	public strassenname : string | null = null;

	/**
	 * Die Hausnummer zur Straße in der die Schule liegt.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.
	 */
	public zusatzHausnummer : string | null = null;

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt.
	 */
	public plz : string | null = null;

	/**
	 * Der Ort in dem die Schule liegt.
	 */
	public ort : string | null = null;

	/**
	 * Die Telefonnummer der Schule.
	 */
	public telefon : string | null = null;

	/**
	 * Die Faxnummer der Schule.
	 */
	public fax : string | null = null;

	/**
	 * Die Mailadresse der Schule.
	 */
	public email : string | null = null;

	/**
	 * Der Name des/der Schuleiters/Schulleiterin.
	 */
	public schulleiter : string | null = null;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung : number = 32000;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.SchulEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kataloge.SchulEintrag'].includes(name);
	}

	public static class = new Class<SchulEintrag>('de.svws_nrw.core.data.kataloge.SchulEintrag');

	public static transpilerFromJSON(json : string): SchulEintrag {
		const obj = JSON.parse(json) as Partial<SchulEintrag>;
		const result = new SchulEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kurzbezeichnung = (obj.kurzbezeichnung === undefined) ? null : obj.kurzbezeichnung === null ? null : obj.kurzbezeichnung;
		result.schulnummerStatistik = (obj.schulnummerStatistik === undefined) ? null : obj.schulnummerStatistik === null ? null : obj.schulnummerStatistik;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		result.idSchulform = (obj.idSchulform === undefined) ? null : obj.idSchulform === null ? null : obj.idSchulform;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.zusatzHausnummer = (obj.zusatzHausnummer === undefined) ? null : obj.zusatzHausnummer === null ? null : obj.zusatzHausnummer;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		result.telefon = (obj.telefon === undefined) ? null : obj.telefon === null ? null : obj.telefon;
		result.fax = (obj.fax === undefined) ? null : obj.fax === null ? null : obj.fax;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.schulleiter = (obj.schulleiter === undefined) ? null : obj.schulleiter === null ? null : obj.schulleiter;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		return result;
	}

	public static transpilerToJSON(obj : SchulEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kurzbezeichnung" : ' + ((obj.kurzbezeichnung === null) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		result += '"schulnummerStatistik" : ' + ((obj.schulnummerStatistik === null) ? 'null' : JSON.stringify(obj.schulnummerStatistik)) + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"idSchulform" : ' + ((obj.idSchulform === null) ? 'null' : obj.idSchulform.toString()) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"zusatzHausnummer" : ' + ((obj.zusatzHausnummer === null) ? 'null' : JSON.stringify(obj.zusatzHausnummer)) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"schulleiter" : ' + ((obj.schulleiter === null) ? 'null' : JSON.stringify(obj.schulleiter)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.kurzbezeichnung !== undefined) {
			result += '"kurzbezeichnung" : ' + ((obj.kurzbezeichnung === null) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		}
		if (obj.schulnummerStatistik !== undefined) {
			result += '"schulnummerStatistik" : ' + ((obj.schulnummerStatistik === null) ? 'null' : JSON.stringify(obj.schulnummerStatistik)) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.idSchulform !== undefined) {
			result += '"idSchulform" : ' + ((obj.idSchulform === null) ? 'null' : obj.idSchulform.toString()) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausnummer !== undefined) {
			result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (obj.zusatzHausnummer !== undefined) {
			result += '"zusatzHausnummer" : ' + ((obj.zusatzHausnummer === null) ? 'null' : JSON.stringify(obj.zusatzHausnummer)) + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (obj.telefon !== undefined) {
			result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (obj.fax !== undefined) {
			result += '"fax" : ' + ((obj.fax === null) ? 'null' : JSON.stringify(obj.fax)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (obj.schulleiter !== undefined) {
			result += '"schulleiter" : ' + ((obj.schulleiter === null) ? 'null' : JSON.stringify(obj.schulleiter)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kataloge_SchulEintrag(obj : unknown) : SchulEintrag {
	return obj as SchulEintrag;
}
