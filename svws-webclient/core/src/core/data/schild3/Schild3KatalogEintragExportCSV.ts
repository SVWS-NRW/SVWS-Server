import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Schild3KatalogEintragExportCSV extends JavaObject {

	/**
	 * Die Datenart
	 */
	public DatenartKrz : string | null = null;

	/**
	 * Der Name des Feldes mit der ID
	 */
	public Feldname : string | null = null;

	/**
	 * Der Text für die Anzeige
	 */
	public AnzeigeText : string | null = null;

	/**
	 * Der Feldtyp
	 */
	public Feldtyp : string | null = null;

	/**
	 * Feldwerte
	 */
	public Feldwerte : string | null = null;

	/**
	 * Ergebniswerte
	 */
	public ErgebnisWerte : string | null = null;

	/**
	 * Der Name des Lookup-Feldes
	 */
	public LookupFeldname : string | null = null;

	/**
	 * Der SQL-Befehl zum Bestimmen des Loopup-Feldwertes
	 */
	public LookupSQLText : string | null = null;

	/**
	 * Die unterstützen Datenbank-Formate
	 */
	public DBFormat : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.Schild3KatalogEintragExportCSV';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragExportCSV'].includes(name);
	}

	public static class = new Class<Schild3KatalogEintragExportCSV>('de.svws_nrw.core.data.schild3.Schild3KatalogEintragExportCSV');

	public static transpilerFromJSON(json : string): Schild3KatalogEintragExportCSV {
		const obj = JSON.parse(json) as Partial<Schild3KatalogEintragExportCSV>;
		const result = new Schild3KatalogEintragExportCSV();
		result.DatenartKrz = (obj.DatenartKrz === undefined) ? null : obj.DatenartKrz === null ? null : obj.DatenartKrz;
		result.Feldname = (obj.Feldname === undefined) ? null : obj.Feldname === null ? null : obj.Feldname;
		result.AnzeigeText = (obj.AnzeigeText === undefined) ? null : obj.AnzeigeText === null ? null : obj.AnzeigeText;
		result.Feldtyp = (obj.Feldtyp === undefined) ? null : obj.Feldtyp === null ? null : obj.Feldtyp;
		result.Feldwerte = (obj.Feldwerte === undefined) ? null : obj.Feldwerte === null ? null : obj.Feldwerte;
		result.ErgebnisWerte = (obj.ErgebnisWerte === undefined) ? null : obj.ErgebnisWerte === null ? null : obj.ErgebnisWerte;
		result.LookupFeldname = (obj.LookupFeldname === undefined) ? null : obj.LookupFeldname === null ? null : obj.LookupFeldname;
		result.LookupSQLText = (obj.LookupSQLText === undefined) ? null : obj.LookupSQLText === null ? null : obj.LookupSQLText;
		result.DBFormat = (obj.DBFormat === undefined) ? null : obj.DBFormat === null ? null : obj.DBFormat;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragExportCSV) : string {
		let result = '{';
		result += '"DatenartKrz" : ' + ((obj.DatenartKrz === null) ? 'null' : JSON.stringify(obj.DatenartKrz)) + ',';
		result += '"Feldname" : ' + ((obj.Feldname === null) ? 'null' : JSON.stringify(obj.Feldname)) + ',';
		result += '"AnzeigeText" : ' + ((obj.AnzeigeText === null) ? 'null' : JSON.stringify(obj.AnzeigeText)) + ',';
		result += '"Feldtyp" : ' + ((obj.Feldtyp === null) ? 'null' : JSON.stringify(obj.Feldtyp)) + ',';
		result += '"Feldwerte" : ' + ((obj.Feldwerte === null) ? 'null' : JSON.stringify(obj.Feldwerte)) + ',';
		result += '"ErgebnisWerte" : ' + ((obj.ErgebnisWerte === null) ? 'null' : JSON.stringify(obj.ErgebnisWerte)) + ',';
		result += '"LookupFeldname" : ' + ((obj.LookupFeldname === null) ? 'null' : JSON.stringify(obj.LookupFeldname)) + ',';
		result += '"LookupSQLText" : ' + ((obj.LookupSQLText === null) ? 'null' : JSON.stringify(obj.LookupSQLText)) + ',';
		result += '"DBFormat" : ' + ((obj.DBFormat === null) ? 'null' : JSON.stringify(obj.DBFormat)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragExportCSV>) : string {
		let result = '{';
		if (obj.DatenartKrz !== undefined) {
			result += '"DatenartKrz" : ' + ((obj.DatenartKrz === null) ? 'null' : JSON.stringify(obj.DatenartKrz)) + ',';
		}
		if (obj.Feldname !== undefined) {
			result += '"Feldname" : ' + ((obj.Feldname === null) ? 'null' : JSON.stringify(obj.Feldname)) + ',';
		}
		if (obj.AnzeigeText !== undefined) {
			result += '"AnzeigeText" : ' + ((obj.AnzeigeText === null) ? 'null' : JSON.stringify(obj.AnzeigeText)) + ',';
		}
		if (obj.Feldtyp !== undefined) {
			result += '"Feldtyp" : ' + ((obj.Feldtyp === null) ? 'null' : JSON.stringify(obj.Feldtyp)) + ',';
		}
		if (obj.Feldwerte !== undefined) {
			result += '"Feldwerte" : ' + ((obj.Feldwerte === null) ? 'null' : JSON.stringify(obj.Feldwerte)) + ',';
		}
		if (obj.ErgebnisWerte !== undefined) {
			result += '"ErgebnisWerte" : ' + ((obj.ErgebnisWerte === null) ? 'null' : JSON.stringify(obj.ErgebnisWerte)) + ',';
		}
		if (obj.LookupFeldname !== undefined) {
			result += '"LookupFeldname" : ' + ((obj.LookupFeldname === null) ? 'null' : JSON.stringify(obj.LookupFeldname)) + ',';
		}
		if (obj.LookupSQLText !== undefined) {
			result += '"LookupSQLText" : ' + ((obj.LookupSQLText === null) ? 'null' : JSON.stringify(obj.LookupSQLText)) + ',';
		}
		if (obj.DBFormat !== undefined) {
			result += '"DBFormat" : ' + ((obj.DBFormat === null) ? 'null' : JSON.stringify(obj.DBFormat)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragExportCSV(obj : unknown) : Schild3KatalogEintragExportCSV {
	return obj as Schild3KatalogEintragExportCSV;
}
