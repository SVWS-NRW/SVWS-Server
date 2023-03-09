import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragSchuelerImportExport extends JavaObject {

	/**
	 * Tabelle mit den Daten für den Import-Export 
	 */
	public Tabelle : string | null = null;

	/**
	 * Anzeigename 
	 */
	public TabellenAnzeige : string | null = null;

	/**
	 * Master-Tabelle 
	 */
	public MasterTable : string | null = null;

	/**
	 * SQL-Befehl für dem Export 
	 */
	public ExpCmd : string | null = null;

	/**
	 * SQL-Befehl zum Ermitteln der Feldnamen 
	 */
	public SrcGetFieldsSQL : string | null = null;

	/**
	 * SQL-Befehl zum Enternen der Daten 
	 */
	public DeleteSQL : string | null = null;

	/**
	 * SQL-Befehl zum Ermitteln der IDs 
	 */
	public DstGetIDSQL : string | null = null;

	/**
	 * Hauptfeld 
	 */
	public HauptFeld : string | null = null;

	/**
	 * Detail-Feld 
	 */
	public DetailFeld : string | null = null;

	/**
	 * Reihenfolge 
	 */
	public Reihenfolge : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragSchuelerImportExport'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragSchuelerImportExport {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragSchuelerImportExport();
		result.Tabelle = typeof obj.Tabelle === "undefined" ? null : obj.Tabelle === null ? null : obj.Tabelle;
		result.TabellenAnzeige = typeof obj.TabellenAnzeige === "undefined" ? null : obj.TabellenAnzeige === null ? null : obj.TabellenAnzeige;
		result.MasterTable = typeof obj.MasterTable === "undefined" ? null : obj.MasterTable === null ? null : obj.MasterTable;
		result.ExpCmd = typeof obj.ExpCmd === "undefined" ? null : obj.ExpCmd === null ? null : obj.ExpCmd;
		result.SrcGetFieldsSQL = typeof obj.SrcGetFieldsSQL === "undefined" ? null : obj.SrcGetFieldsSQL === null ? null : obj.SrcGetFieldsSQL;
		result.DeleteSQL = typeof obj.DeleteSQL === "undefined" ? null : obj.DeleteSQL === null ? null : obj.DeleteSQL;
		result.DstGetIDSQL = typeof obj.DstGetIDSQL === "undefined" ? null : obj.DstGetIDSQL === null ? null : obj.DstGetIDSQL;
		result.HauptFeld = typeof obj.HauptFeld === "undefined" ? null : obj.HauptFeld === null ? null : obj.HauptFeld;
		result.DetailFeld = typeof obj.DetailFeld === "undefined" ? null : obj.DetailFeld === null ? null : obj.DetailFeld;
		result.Reihenfolge = typeof obj.Reihenfolge === "undefined" ? null : obj.Reihenfolge === null ? null : obj.Reihenfolge;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragSchuelerImportExport) : string {
		let result = '{';
		result += '"Tabelle" : ' + ((!obj.Tabelle) ? 'null' : '"' + obj.Tabelle + '"') + ',';
		result += '"TabellenAnzeige" : ' + ((!obj.TabellenAnzeige) ? 'null' : '"' + obj.TabellenAnzeige + '"') + ',';
		result += '"MasterTable" : ' + ((!obj.MasterTable) ? 'null' : '"' + obj.MasterTable + '"') + ',';
		result += '"ExpCmd" : ' + ((!obj.ExpCmd) ? 'null' : '"' + obj.ExpCmd + '"') + ',';
		result += '"SrcGetFieldsSQL" : ' + ((!obj.SrcGetFieldsSQL) ? 'null' : '"' + obj.SrcGetFieldsSQL + '"') + ',';
		result += '"DeleteSQL" : ' + ((!obj.DeleteSQL) ? 'null' : '"' + obj.DeleteSQL + '"') + ',';
		result += '"DstGetIDSQL" : ' + ((!obj.DstGetIDSQL) ? 'null' : '"' + obj.DstGetIDSQL + '"') + ',';
		result += '"HauptFeld" : ' + ((!obj.HauptFeld) ? 'null' : '"' + obj.HauptFeld + '"') + ',';
		result += '"DetailFeld" : ' + ((!obj.DetailFeld) ? 'null' : '"' + obj.DetailFeld + '"') + ',';
		result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragSchuelerImportExport>) : string {
		let result = '{';
		if (typeof obj.Tabelle !== "undefined") {
			result += '"Tabelle" : ' + ((!obj.Tabelle) ? 'null' : '"' + obj.Tabelle + '"') + ',';
		}
		if (typeof obj.TabellenAnzeige !== "undefined") {
			result += '"TabellenAnzeige" : ' + ((!obj.TabellenAnzeige) ? 'null' : '"' + obj.TabellenAnzeige + '"') + ',';
		}
		if (typeof obj.MasterTable !== "undefined") {
			result += '"MasterTable" : ' + ((!obj.MasterTable) ? 'null' : '"' + obj.MasterTable + '"') + ',';
		}
		if (typeof obj.ExpCmd !== "undefined") {
			result += '"ExpCmd" : ' + ((!obj.ExpCmd) ? 'null' : '"' + obj.ExpCmd + '"') + ',';
		}
		if (typeof obj.SrcGetFieldsSQL !== "undefined") {
			result += '"SrcGetFieldsSQL" : ' + ((!obj.SrcGetFieldsSQL) ? 'null' : '"' + obj.SrcGetFieldsSQL + '"') + ',';
		}
		if (typeof obj.DeleteSQL !== "undefined") {
			result += '"DeleteSQL" : ' + ((!obj.DeleteSQL) ? 'null' : '"' + obj.DeleteSQL + '"') + ',';
		}
		if (typeof obj.DstGetIDSQL !== "undefined") {
			result += '"DstGetIDSQL" : ' + ((!obj.DstGetIDSQL) ? 'null' : '"' + obj.DstGetIDSQL + '"') + ',';
		}
		if (typeof obj.HauptFeld !== "undefined") {
			result += '"HauptFeld" : ' + ((!obj.HauptFeld) ? 'null' : '"' + obj.HauptFeld + '"') + ',';
		}
		if (typeof obj.DetailFeld !== "undefined") {
			result += '"DetailFeld" : ' + ((!obj.DetailFeld) ? 'null' : '"' + obj.DetailFeld + '"') + ',';
		}
		if (typeof obj.Reihenfolge !== "undefined") {
			result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragSchuelerImportExport(obj : unknown) : Schild3KatalogEintragSchuelerImportExport {
	return obj as Schild3KatalogEintragSchuelerImportExport;
}
