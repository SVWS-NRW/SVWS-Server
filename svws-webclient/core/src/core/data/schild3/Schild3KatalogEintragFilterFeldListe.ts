import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.Schild3KatalogEintragFilterFeldListe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragFilterFeldListe'].includes(name);
	}

	public static class = new Class<Schild3KatalogEintragFilterFeldListe>('de.svws_nrw.core.data.schild3.Schild3KatalogEintragFilterFeldListe');

	public static transpilerFromJSON(json : string): Schild3KatalogEintragFilterFeldListe {
		const obj = JSON.parse(json) as Partial<Schild3KatalogEintragFilterFeldListe>;
		const result = new Schild3KatalogEintragFilterFeldListe();
		result.ID = (obj.ID === undefined) ? null : obj.ID === null ? null : obj.ID;
		result.Bezeichnung = (obj.Bezeichnung === undefined) ? null : obj.Bezeichnung === null ? null : obj.Bezeichnung;
		result.DBFeld = (obj.DBFeld === undefined) ? null : obj.DBFeld === null ? null : obj.DBFeld;
		result.Typ = (obj.Typ === undefined) ? null : obj.Typ === null ? null : obj.Typ;
		result.Werte = (obj.Werte === undefined) ? null : obj.Werte === null ? null : obj.Werte;
		result.StdWert = (obj.StdWert === undefined) ? null : obj.StdWert === null ? null : obj.StdWert;
		result.Operator = (obj.Operator === undefined) ? null : obj.Operator === null ? null : obj.Operator;
		result.Zusatzbedingung = (obj.Zusatzbedingung === undefined) ? null : obj.Zusatzbedingung === null ? null : obj.Zusatzbedingung;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragFilterFeldListe) : string {
		let result = '{';
		result += '"ID" : ' + ((obj.ID === null) ? 'null' : obj.ID.toString()) + ',';
		result += '"Bezeichnung" : ' + ((obj.Bezeichnung === null) ? 'null' : JSON.stringify(obj.Bezeichnung)) + ',';
		result += '"DBFeld" : ' + ((obj.DBFeld === null) ? 'null' : JSON.stringify(obj.DBFeld)) + ',';
		result += '"Typ" : ' + ((obj.Typ === null) ? 'null' : JSON.stringify(obj.Typ)) + ',';
		result += '"Werte" : ' + ((obj.Werte === null) ? 'null' : JSON.stringify(obj.Werte)) + ',';
		result += '"StdWert" : ' + ((obj.StdWert === null) ? 'null' : JSON.stringify(obj.StdWert)) + ',';
		result += '"Operator" : ' + ((obj.Operator === null) ? 'null' : JSON.stringify(obj.Operator)) + ',';
		result += '"Zusatzbedingung" : ' + ((obj.Zusatzbedingung === null) ? 'null' : JSON.stringify(obj.Zusatzbedingung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragFilterFeldListe>) : string {
		let result = '{';
		if (obj.ID !== undefined) {
			result += '"ID" : ' + ((obj.ID === null) ? 'null' : obj.ID.toString()) + ',';
		}
		if (obj.Bezeichnung !== undefined) {
			result += '"Bezeichnung" : ' + ((obj.Bezeichnung === null) ? 'null' : JSON.stringify(obj.Bezeichnung)) + ',';
		}
		if (obj.DBFeld !== undefined) {
			result += '"DBFeld" : ' + ((obj.DBFeld === null) ? 'null' : JSON.stringify(obj.DBFeld)) + ',';
		}
		if (obj.Typ !== undefined) {
			result += '"Typ" : ' + ((obj.Typ === null) ? 'null' : JSON.stringify(obj.Typ)) + ',';
		}
		if (obj.Werte !== undefined) {
			result += '"Werte" : ' + ((obj.Werte === null) ? 'null' : JSON.stringify(obj.Werte)) + ',';
		}
		if (obj.StdWert !== undefined) {
			result += '"StdWert" : ' + ((obj.StdWert === null) ? 'null' : JSON.stringify(obj.StdWert)) + ',';
		}
		if (obj.Operator !== undefined) {
			result += '"Operator" : ' + ((obj.Operator === null) ? 'null' : JSON.stringify(obj.Operator)) + ',';
		}
		if (obj.Zusatzbedingung !== undefined) {
			result += '"Zusatzbedingung" : ' + ((obj.Zusatzbedingung === null) ? 'null' : JSON.stringify(obj.Zusatzbedingung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragFilterFeldListe(obj : unknown) : Schild3KatalogEintragFilterFeldListe {
	return obj as Schild3KatalogEintragFilterFeldListe;
}
