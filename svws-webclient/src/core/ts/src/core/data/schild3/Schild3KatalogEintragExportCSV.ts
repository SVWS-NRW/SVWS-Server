import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

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


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragExportCSV'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragExportCSV {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragExportCSV();
		result.DatenartKrz = typeof obj.DatenartKrz === "undefined" ? null : obj.DatenartKrz === null ? null : obj.DatenartKrz;
		result.Feldname = typeof obj.Feldname === "undefined" ? null : obj.Feldname === null ? null : obj.Feldname;
		result.AnzeigeText = typeof obj.AnzeigeText === "undefined" ? null : obj.AnzeigeText === null ? null : obj.AnzeigeText;
		result.Feldtyp = typeof obj.Feldtyp === "undefined" ? null : obj.Feldtyp === null ? null : obj.Feldtyp;
		result.Feldwerte = typeof obj.Feldwerte === "undefined" ? null : obj.Feldwerte === null ? null : obj.Feldwerte;
		result.ErgebnisWerte = typeof obj.ErgebnisWerte === "undefined" ? null : obj.ErgebnisWerte === null ? null : obj.ErgebnisWerte;
		result.LookupFeldname = typeof obj.LookupFeldname === "undefined" ? null : obj.LookupFeldname === null ? null : obj.LookupFeldname;
		result.LookupSQLText = typeof obj.LookupSQLText === "undefined" ? null : obj.LookupSQLText === null ? null : obj.LookupSQLText;
		result.DBFormat = typeof obj.DBFormat === "undefined" ? null : obj.DBFormat === null ? null : obj.DBFormat;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragExportCSV) : string {
		let result = '{';
		result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : '"' + obj.DatenartKrz + '"') + ',';
		result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname + '"') + ',';
		result += '"AnzeigeText" : ' + ((!obj.AnzeigeText) ? 'null' : '"' + obj.AnzeigeText + '"') + ',';
		result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp + '"') + ',';
		result += '"Feldwerte" : ' + ((!obj.Feldwerte) ? 'null' : '"' + obj.Feldwerte + '"') + ',';
		result += '"ErgebnisWerte" : ' + ((!obj.ErgebnisWerte) ? 'null' : '"' + obj.ErgebnisWerte + '"') + ',';
		result += '"LookupFeldname" : ' + ((!obj.LookupFeldname) ? 'null' : '"' + obj.LookupFeldname + '"') + ',';
		result += '"LookupSQLText" : ' + ((!obj.LookupSQLText) ? 'null' : '"' + obj.LookupSQLText + '"') + ',';
		result += '"DBFormat" : ' + ((!obj.DBFormat) ? 'null' : '"' + obj.DBFormat + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragExportCSV>) : string {
		let result = '{';
		if (typeof obj.DatenartKrz !== "undefined") {
			result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : '"' + obj.DatenartKrz + '"') + ',';
		}
		if (typeof obj.Feldname !== "undefined") {
			result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname + '"') + ',';
		}
		if (typeof obj.AnzeigeText !== "undefined") {
			result += '"AnzeigeText" : ' + ((!obj.AnzeigeText) ? 'null' : '"' + obj.AnzeigeText + '"') + ',';
		}
		if (typeof obj.Feldtyp !== "undefined") {
			result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp + '"') + ',';
		}
		if (typeof obj.Feldwerte !== "undefined") {
			result += '"Feldwerte" : ' + ((!obj.Feldwerte) ? 'null' : '"' + obj.Feldwerte + '"') + ',';
		}
		if (typeof obj.ErgebnisWerte !== "undefined") {
			result += '"ErgebnisWerte" : ' + ((!obj.ErgebnisWerte) ? 'null' : '"' + obj.ErgebnisWerte + '"') + ',';
		}
		if (typeof obj.LookupFeldname !== "undefined") {
			result += '"LookupFeldname" : ' + ((!obj.LookupFeldname) ? 'null' : '"' + obj.LookupFeldname + '"') + ',';
		}
		if (typeof obj.LookupSQLText !== "undefined") {
			result += '"LookupSQLText" : ' + ((!obj.LookupSQLText) ? 'null' : '"' + obj.LookupSQLText + '"') + ',';
		}
		if (typeof obj.DBFormat !== "undefined") {
			result += '"DBFormat" : ' + ((!obj.DBFormat) ? 'null' : '"' + obj.DBFormat + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragExportCSV(obj : unknown) : Schild3KatalogEintragExportCSV {
	return obj as Schild3KatalogEintragExportCSV;
}
