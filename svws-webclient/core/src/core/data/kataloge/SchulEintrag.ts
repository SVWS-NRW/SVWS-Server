import { JavaObject } from '../../../java/lang/JavaObject';

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
	 * Die Schulnummer der Schule.
	 */
	public schulnummer : string = "";

	/**
	 * Der Name des Schule.
	 */
	public name : string = "";

	/**
	 * Die ID der Schulform.
	 */
	public schulformID : number | null = null;

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
	public hausnummerZusatz : string | null = null;

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
	 * Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht.
	 */
	public istAenderbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kataloge.SchulEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulEintrag {
		const obj = JSON.parse(json);
		const result = new SchulEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kurzbezeichnung = typeof obj.kurzbezeichnung === "undefined" ? null : obj.kurzbezeichnung === null ? null : obj.kurzbezeichnung;
		if (typeof obj.schulnummer === "undefined")
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		result.schulformID = typeof obj.schulformID === "undefined" ? null : obj.schulformID === null ? null : obj.schulformID;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz === null ? null : obj.plz;
		result.ort = typeof obj.ort === "undefined" ? null : obj.ort === null ? null : obj.ort;
		result.telefon = typeof obj.telefon === "undefined" ? null : obj.telefon === null ? null : obj.telefon;
		result.fax = typeof obj.fax === "undefined" ? null : obj.fax === null ? null : obj.fax;
		result.email = typeof obj.email === "undefined" ? null : obj.email === null ? null : obj.email;
		result.schulleiter = typeof obj.schulleiter === "undefined" ? null : obj.schulleiter === null ? null : obj.schulleiter;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (typeof obj.istAenderbar === "undefined")
			 throw new Error('invalid json format, missing attribute istAenderbar');
		result.istAenderbar = obj.istAenderbar;
		return result;
	}

	public static transpilerToJSON(obj : SchulEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kurzbezeichnung" : ' + ((!obj.kurzbezeichnung) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer!) + ',';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"schulformID" : ' + ((!obj.schulformID) ? 'null' : obj.schulformID) + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((!obj.ort) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"telefon" : ' + ((!obj.telefon) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"fax" : ' + ((!obj.fax) ? 'null' : JSON.stringify(obj.fax)) + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"schulleiter" : ' + ((!obj.schulleiter) ? 'null' : JSON.stringify(obj.schulleiter)) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (typeof obj.kurzbezeichnung !== "undefined") {
			result += '"kurzbezeichnung" : ' + ((!obj.kurzbezeichnung) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		}
		if (typeof obj.schulnummer !== "undefined") {
			result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer!) + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (typeof obj.schulformID !== "undefined") {
			result += '"schulformID" : ' + ((!obj.schulformID) ? 'null' : obj.schulformID) + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (typeof obj.plz !== "undefined") {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (typeof obj.ort !== "undefined") {
			result += '"ort" : ' + ((!obj.ort) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (typeof obj.telefon !== "undefined") {
			result += '"telefon" : ' + ((!obj.telefon) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (typeof obj.fax !== "undefined") {
			result += '"fax" : ' + ((!obj.fax) ? 'null' : JSON.stringify(obj.fax)) + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (typeof obj.schulleiter !== "undefined") {
			result += '"schulleiter" : ' + ((!obj.schulleiter) ? 'null' : JSON.stringify(obj.schulleiter)) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		if (typeof obj.istAenderbar !== "undefined") {
			result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kataloge_SchulEintrag(obj : unknown) : SchulEintrag {
	return obj as SchulEintrag;
}
