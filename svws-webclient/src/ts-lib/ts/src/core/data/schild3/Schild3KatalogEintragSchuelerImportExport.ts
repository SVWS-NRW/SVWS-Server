import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragSchuelerImportExport extends JavaObject {

	/**
	 * Tabelle mit den Daten für den Import-Export 
	 */
	public Tabelle : String | null = null;

	/**
	 * Anzeigename 
	 */
	public TabellenAnzeige : String | null = null;

	/**
	 * Master-Tabelle 
	 */
	public MasterTable : String | null = null;

	/**
	 * SQL-Befehl für dem Export 
	 */
	public ExpCmd : String | null = null;

	/**
	 * SQL-Befehl zum Ermitteln der Feldnamen 
	 */
	public SrcGetFieldsSQL : String | null = null;

	/**
	 * SQL-Befehl zum Enternen der Daten 
	 */
	public DeleteSQL : String | null = null;

	/**
	 * SQL-Befehl zum Ermitteln der IDs 
	 */
	public DstGetIDSQL : String | null = null;

	/**
	 * Hauptfeld 
	 */
	public HauptFeld : String | null = null;

	/**
	 * Detail-Feld 
	 */
	public DetailFeld : String | null = null;

	/**
	 * Reihenfolge 
	 */
	public Reihenfolge : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragSchuelerImportExport'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragSchuelerImportExport {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragSchuelerImportExport();
		result.Tabelle = typeof obj.Tabelle === "undefined" ? null : obj.Tabelle === null ? null : String(obj.Tabelle);
		result.TabellenAnzeige = typeof obj.TabellenAnzeige === "undefined" ? null : obj.TabellenAnzeige === null ? null : String(obj.TabellenAnzeige);
		result.MasterTable = typeof obj.MasterTable === "undefined" ? null : obj.MasterTable === null ? null : String(obj.MasterTable);
		result.ExpCmd = typeof obj.ExpCmd === "undefined" ? null : obj.ExpCmd === null ? null : String(obj.ExpCmd);
		result.SrcGetFieldsSQL = typeof obj.SrcGetFieldsSQL === "undefined" ? null : obj.SrcGetFieldsSQL === null ? null : String(obj.SrcGetFieldsSQL);
		result.DeleteSQL = typeof obj.DeleteSQL === "undefined" ? null : obj.DeleteSQL === null ? null : String(obj.DeleteSQL);
		result.DstGetIDSQL = typeof obj.DstGetIDSQL === "undefined" ? null : obj.DstGetIDSQL === null ? null : String(obj.DstGetIDSQL);
		result.HauptFeld = typeof obj.HauptFeld === "undefined" ? null : obj.HauptFeld === null ? null : String(obj.HauptFeld);
		result.DetailFeld = typeof obj.DetailFeld === "undefined" ? null : obj.DetailFeld === null ? null : String(obj.DetailFeld);
		result.Reihenfolge = typeof obj.Reihenfolge === "undefined" ? null : obj.Reihenfolge === null ? null : Number(obj.Reihenfolge);
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragSchuelerImportExport) : string {
		let result = '{';
		result += '"Tabelle" : ' + ((!obj.Tabelle) ? 'null' : '"' + obj.Tabelle.valueOf() + '"') + ',';
		result += '"TabellenAnzeige" : ' + ((!obj.TabellenAnzeige) ? 'null' : '"' + obj.TabellenAnzeige.valueOf() + '"') + ',';
		result += '"MasterTable" : ' + ((!obj.MasterTable) ? 'null' : '"' + obj.MasterTable.valueOf() + '"') + ',';
		result += '"ExpCmd" : ' + ((!obj.ExpCmd) ? 'null' : '"' + obj.ExpCmd.valueOf() + '"') + ',';
		result += '"SrcGetFieldsSQL" : ' + ((!obj.SrcGetFieldsSQL) ? 'null' : '"' + obj.SrcGetFieldsSQL.valueOf() + '"') + ',';
		result += '"DeleteSQL" : ' + ((!obj.DeleteSQL) ? 'null' : '"' + obj.DeleteSQL.valueOf() + '"') + ',';
		result += '"DstGetIDSQL" : ' + ((!obj.DstGetIDSQL) ? 'null' : '"' + obj.DstGetIDSQL.valueOf() + '"') + ',';
		result += '"HauptFeld" : ' + ((!obj.HauptFeld) ? 'null' : '"' + obj.HauptFeld.valueOf() + '"') + ',';
		result += '"DetailFeld" : ' + ((!obj.DetailFeld) ? 'null' : '"' + obj.DetailFeld.valueOf() + '"') + ',';
		result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragSchuelerImportExport>) : string {
		let result = '{';
		if (typeof obj.Tabelle !== "undefined") {
			result += '"Tabelle" : ' + ((!obj.Tabelle) ? 'null' : '"' + obj.Tabelle.valueOf() + '"') + ',';
		}
		if (typeof obj.TabellenAnzeige !== "undefined") {
			result += '"TabellenAnzeige" : ' + ((!obj.TabellenAnzeige) ? 'null' : '"' + obj.TabellenAnzeige.valueOf() + '"') + ',';
		}
		if (typeof obj.MasterTable !== "undefined") {
			result += '"MasterTable" : ' + ((!obj.MasterTable) ? 'null' : '"' + obj.MasterTable.valueOf() + '"') + ',';
		}
		if (typeof obj.ExpCmd !== "undefined") {
			result += '"ExpCmd" : ' + ((!obj.ExpCmd) ? 'null' : '"' + obj.ExpCmd.valueOf() + '"') + ',';
		}
		if (typeof obj.SrcGetFieldsSQL !== "undefined") {
			result += '"SrcGetFieldsSQL" : ' + ((!obj.SrcGetFieldsSQL) ? 'null' : '"' + obj.SrcGetFieldsSQL.valueOf() + '"') + ',';
		}
		if (typeof obj.DeleteSQL !== "undefined") {
			result += '"DeleteSQL" : ' + ((!obj.DeleteSQL) ? 'null' : '"' + obj.DeleteSQL.valueOf() + '"') + ',';
		}
		if (typeof obj.DstGetIDSQL !== "undefined") {
			result += '"DstGetIDSQL" : ' + ((!obj.DstGetIDSQL) ? 'null' : '"' + obj.DstGetIDSQL.valueOf() + '"') + ',';
		}
		if (typeof obj.HauptFeld !== "undefined") {
			result += '"HauptFeld" : ' + ((!obj.HauptFeld) ? 'null' : '"' + obj.HauptFeld.valueOf() + '"') + ',';
		}
		if (typeof obj.DetailFeld !== "undefined") {
			result += '"DetailFeld" : ' + ((!obj.DetailFeld) ? 'null' : '"' + obj.DetailFeld.valueOf() + '"') + ',';
		}
		if (typeof obj.Reihenfolge !== "undefined") {
			result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragSchuelerImportExport(obj : unknown) : Schild3KatalogEintragSchuelerImportExport {
	return obj as Schild3KatalogEintragSchuelerImportExport;
}
