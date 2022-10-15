import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragExportCSV extends JavaObject {

	public DatenartKrz : String | null = null;

	public Feldname : String | null = null;

	public AnzeigeText : String | null = null;

	public Feldtyp : String | null = null;

	public Feldwerte : String | null = null;

	public ErgebnisWerte : String | null = null;

	public LookupFeldname : String | null = null;

	public LookupSQLText : String | null = null;

	public DBFormat : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragExportCSV'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragExportCSV {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragExportCSV();
		result.DatenartKrz = typeof obj.DatenartKrz === "undefined" ? null : obj.DatenartKrz;
		result.Feldname = typeof obj.Feldname === "undefined" ? null : obj.Feldname;
		result.AnzeigeText = typeof obj.AnzeigeText === "undefined" ? null : obj.AnzeigeText;
		result.Feldtyp = typeof obj.Feldtyp === "undefined" ? null : obj.Feldtyp;
		result.Feldwerte = typeof obj.Feldwerte === "undefined" ? null : obj.Feldwerte;
		result.ErgebnisWerte = typeof obj.ErgebnisWerte === "undefined" ? null : obj.ErgebnisWerte;
		result.LookupFeldname = typeof obj.LookupFeldname === "undefined" ? null : obj.LookupFeldname;
		result.LookupSQLText = typeof obj.LookupSQLText === "undefined" ? null : obj.LookupSQLText;
		result.DBFormat = typeof obj.DBFormat === "undefined" ? null : obj.DBFormat;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragExportCSV) : string {
		let result = '{';
		result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : '"' + obj.DatenartKrz.valueOf() + '"') + ',';
		result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname.valueOf() + '"') + ',';
		result += '"AnzeigeText" : ' + ((!obj.AnzeigeText) ? 'null' : '"' + obj.AnzeigeText.valueOf() + '"') + ',';
		result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp.valueOf() + '"') + ',';
		result += '"Feldwerte" : ' + ((!obj.Feldwerte) ? 'null' : '"' + obj.Feldwerte.valueOf() + '"') + ',';
		result += '"ErgebnisWerte" : ' + ((!obj.ErgebnisWerte) ? 'null' : '"' + obj.ErgebnisWerte.valueOf() + '"') + ',';
		result += '"LookupFeldname" : ' + ((!obj.LookupFeldname) ? 'null' : '"' + obj.LookupFeldname.valueOf() + '"') + ',';
		result += '"LookupSQLText" : ' + ((!obj.LookupSQLText) ? 'null' : '"' + obj.LookupSQLText.valueOf() + '"') + ',';
		result += '"DBFormat" : ' + ((!obj.DBFormat) ? 'null' : '"' + obj.DBFormat.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragExportCSV>) : string {
		let result = '{';
		if (typeof obj.DatenartKrz !== "undefined") {
			result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : '"' + obj.DatenartKrz.valueOf() + '"') + ',';
		}
		if (typeof obj.Feldname !== "undefined") {
			result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname.valueOf() + '"') + ',';
		}
		if (typeof obj.AnzeigeText !== "undefined") {
			result += '"AnzeigeText" : ' + ((!obj.AnzeigeText) ? 'null' : '"' + obj.AnzeigeText.valueOf() + '"') + ',';
		}
		if (typeof obj.Feldtyp !== "undefined") {
			result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp.valueOf() + '"') + ',';
		}
		if (typeof obj.Feldwerte !== "undefined") {
			result += '"Feldwerte" : ' + ((!obj.Feldwerte) ? 'null' : '"' + obj.Feldwerte.valueOf() + '"') + ',';
		}
		if (typeof obj.ErgebnisWerte !== "undefined") {
			result += '"ErgebnisWerte" : ' + ((!obj.ErgebnisWerte) ? 'null' : '"' + obj.ErgebnisWerte.valueOf() + '"') + ',';
		}
		if (typeof obj.LookupFeldname !== "undefined") {
			result += '"LookupFeldname" : ' + ((!obj.LookupFeldname) ? 'null' : '"' + obj.LookupFeldname.valueOf() + '"') + ',';
		}
		if (typeof obj.LookupSQLText !== "undefined") {
			result += '"LookupSQLText" : ' + ((!obj.LookupSQLText) ? 'null' : '"' + obj.LookupSQLText.valueOf() + '"') + ',';
		}
		if (typeof obj.DBFormat !== "undefined") {
			result += '"DBFormat" : ' + ((!obj.DBFormat) ? 'null' : '"' + obj.DBFormat.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragExportCSV(obj : unknown) : Schild3KatalogEintragExportCSV {
	return obj as Schild3KatalogEintragExportCSV;
}
