import { JavaObject } from '../../../java/lang/JavaObject';

export class Schild3KatalogEintragFilterFeldListe extends JavaObject {

	/**
	 * ID für den Eintrag welche Felder im Attributsfilter zur Verfügung stehen.
	 */
	public ID : number | null = null;

	/**
	 * Bezeichnung im Attributsfilter
	 */
	public Bezeichnung : string | null = null;

	/**
	 * Datenbankfeld im Attributsfilter
	 */
	public DBFeld : string | null = null;

	/**
	 * Typ des Feldes im Attributsfilter
	 */
	public Typ : string | null = null;

	/**
	 * Mögliche Werte des Feldes im Attributsfilter
	 */
	public Werte : string | null = null;

	/**
	 * Standardwert im Attributsfilter
	 */
	public StdWert : string | null = null;

	/**
	 * Operator  im Attributsfilter (größer-kleiner)
	 */
	public Operator : string | null = null;

	/**
	 * Zusatzbedingung im Attributsfilter
	 */
	public Zusatzbedingung : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragFilterFeldListe'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragFilterFeldListe {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragFilterFeldListe();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID === null ? null : obj.ID;
		result.Bezeichnung = typeof obj.Bezeichnung === "undefined" ? null : obj.Bezeichnung === null ? null : obj.Bezeichnung;
		result.DBFeld = typeof obj.DBFeld === "undefined" ? null : obj.DBFeld === null ? null : obj.DBFeld;
		result.Typ = typeof obj.Typ === "undefined" ? null : obj.Typ === null ? null : obj.Typ;
		result.Werte = typeof obj.Werte === "undefined" ? null : obj.Werte === null ? null : obj.Werte;
		result.StdWert = typeof obj.StdWert === "undefined" ? null : obj.StdWert === null ? null : obj.StdWert;
		result.Operator = typeof obj.Operator === "undefined" ? null : obj.Operator === null ? null : obj.Operator;
		result.Zusatzbedingung = typeof obj.Zusatzbedingung === "undefined" ? null : obj.Zusatzbedingung === null ? null : obj.Zusatzbedingung;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragFilterFeldListe) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung + '"') + ',';
		result += '"DBFeld" : ' + ((!obj.DBFeld) ? 'null' : '"' + obj.DBFeld + '"') + ',';
		result += '"Typ" : ' + ((!obj.Typ) ? 'null' : '"' + obj.Typ + '"') + ',';
		result += '"Werte" : ' + ((!obj.Werte) ? 'null' : '"' + obj.Werte + '"') + ',';
		result += '"StdWert" : ' + ((!obj.StdWert) ? 'null' : '"' + obj.StdWert + '"') + ',';
		result += '"Operator" : ' + ((!obj.Operator) ? 'null' : '"' + obj.Operator + '"') + ',';
		result += '"Zusatzbedingung" : ' + ((!obj.Zusatzbedingung) ? 'null' : '"' + obj.Zusatzbedingung + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragFilterFeldListe>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		}
		if (typeof obj.Bezeichnung !== "undefined") {
			result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung + '"') + ',';
		}
		if (typeof obj.DBFeld !== "undefined") {
			result += '"DBFeld" : ' + ((!obj.DBFeld) ? 'null' : '"' + obj.DBFeld + '"') + ',';
		}
		if (typeof obj.Typ !== "undefined") {
			result += '"Typ" : ' + ((!obj.Typ) ? 'null' : '"' + obj.Typ + '"') + ',';
		}
		if (typeof obj.Werte !== "undefined") {
			result += '"Werte" : ' + ((!obj.Werte) ? 'null' : '"' + obj.Werte + '"') + ',';
		}
		if (typeof obj.StdWert !== "undefined") {
			result += '"StdWert" : ' + ((!obj.StdWert) ? 'null' : '"' + obj.StdWert + '"') + ',';
		}
		if (typeof obj.Operator !== "undefined") {
			result += '"Operator" : ' + ((!obj.Operator) ? 'null' : '"' + obj.Operator + '"') + ',';
		}
		if (typeof obj.Zusatzbedingung !== "undefined") {
			result += '"Zusatzbedingung" : ' + ((!obj.Zusatzbedingung) ? 'null' : '"' + obj.Zusatzbedingung + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragFilterFeldListe(obj : unknown) : Schild3KatalogEintragFilterFeldListe {
	return obj as Schild3KatalogEintragFilterFeldListe;
}
