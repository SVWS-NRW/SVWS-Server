import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.Schild3KatalogEintragSchuelerImportExport';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.Schild3KatalogEintragSchuelerImportExport'].includes(name);
	}

	public static class = new Class<Schild3KatalogEintragSchuelerImportExport>('de.svws_nrw.core.data.schild3.Schild3KatalogEintragSchuelerImportExport');

	public static transpilerFromJSON(json : string): Schild3KatalogEintragSchuelerImportExport {
		const obj = JSON.parse(json) as Partial<Schild3KatalogEintragSchuelerImportExport>;
		const result = new Schild3KatalogEintragSchuelerImportExport();
		result.Tabelle = (obj.Tabelle === undefined) ? null : obj.Tabelle === null ? null : obj.Tabelle;
		result.TabellenAnzeige = (obj.TabellenAnzeige === undefined) ? null : obj.TabellenAnzeige === null ? null : obj.TabellenAnzeige;
		result.MasterTable = (obj.MasterTable === undefined) ? null : obj.MasterTable === null ? null : obj.MasterTable;
		result.ExpCmd = (obj.ExpCmd === undefined) ? null : obj.ExpCmd === null ? null : obj.ExpCmd;
		result.SrcGetFieldsSQL = (obj.SrcGetFieldsSQL === undefined) ? null : obj.SrcGetFieldsSQL === null ? null : obj.SrcGetFieldsSQL;
		result.DeleteSQL = (obj.DeleteSQL === undefined) ? null : obj.DeleteSQL === null ? null : obj.DeleteSQL;
		result.DstGetIDSQL = (obj.DstGetIDSQL === undefined) ? null : obj.DstGetIDSQL === null ? null : obj.DstGetIDSQL;
		result.HauptFeld = (obj.HauptFeld === undefined) ? null : obj.HauptFeld === null ? null : obj.HauptFeld;
		result.DetailFeld = (obj.DetailFeld === undefined) ? null : obj.DetailFeld === null ? null : obj.DetailFeld;
		result.Reihenfolge = (obj.Reihenfolge === undefined) ? null : obj.Reihenfolge === null ? null : obj.Reihenfolge;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragSchuelerImportExport) : string {
		let result = '{';
		result += '"Tabelle" : ' + ((obj.Tabelle === null) ? 'null' : JSON.stringify(obj.Tabelle)) + ',';
		result += '"TabellenAnzeige" : ' + ((obj.TabellenAnzeige === null) ? 'null' : JSON.stringify(obj.TabellenAnzeige)) + ',';
		result += '"MasterTable" : ' + ((obj.MasterTable === null) ? 'null' : JSON.stringify(obj.MasterTable)) + ',';
		result += '"ExpCmd" : ' + ((obj.ExpCmd === null) ? 'null' : JSON.stringify(obj.ExpCmd)) + ',';
		result += '"SrcGetFieldsSQL" : ' + ((obj.SrcGetFieldsSQL === null) ? 'null' : JSON.stringify(obj.SrcGetFieldsSQL)) + ',';
		result += '"DeleteSQL" : ' + ((obj.DeleteSQL === null) ? 'null' : JSON.stringify(obj.DeleteSQL)) + ',';
		result += '"DstGetIDSQL" : ' + ((obj.DstGetIDSQL === null) ? 'null' : JSON.stringify(obj.DstGetIDSQL)) + ',';
		result += '"HauptFeld" : ' + ((obj.HauptFeld === null) ? 'null' : JSON.stringify(obj.HauptFeld)) + ',';
		result += '"DetailFeld" : ' + ((obj.DetailFeld === null) ? 'null' : JSON.stringify(obj.DetailFeld)) + ',';
		result += '"Reihenfolge" : ' + ((obj.Reihenfolge === null) ? 'null' : obj.Reihenfolge.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragSchuelerImportExport>) : string {
		let result = '{';
		if (obj.Tabelle !== undefined) {
			result += '"Tabelle" : ' + ((obj.Tabelle === null) ? 'null' : JSON.stringify(obj.Tabelle)) + ',';
		}
		if (obj.TabellenAnzeige !== undefined) {
			result += '"TabellenAnzeige" : ' + ((obj.TabellenAnzeige === null) ? 'null' : JSON.stringify(obj.TabellenAnzeige)) + ',';
		}
		if (obj.MasterTable !== undefined) {
			result += '"MasterTable" : ' + ((obj.MasterTable === null) ? 'null' : JSON.stringify(obj.MasterTable)) + ',';
		}
		if (obj.ExpCmd !== undefined) {
			result += '"ExpCmd" : ' + ((obj.ExpCmd === null) ? 'null' : JSON.stringify(obj.ExpCmd)) + ',';
		}
		if (obj.SrcGetFieldsSQL !== undefined) {
			result += '"SrcGetFieldsSQL" : ' + ((obj.SrcGetFieldsSQL === null) ? 'null' : JSON.stringify(obj.SrcGetFieldsSQL)) + ',';
		}
		if (obj.DeleteSQL !== undefined) {
			result += '"DeleteSQL" : ' + ((obj.DeleteSQL === null) ? 'null' : JSON.stringify(obj.DeleteSQL)) + ',';
		}
		if (obj.DstGetIDSQL !== undefined) {
			result += '"DstGetIDSQL" : ' + ((obj.DstGetIDSQL === null) ? 'null' : JSON.stringify(obj.DstGetIDSQL)) + ',';
		}
		if (obj.HauptFeld !== undefined) {
			result += '"HauptFeld" : ' + ((obj.HauptFeld === null) ? 'null' : JSON.stringify(obj.HauptFeld)) + ',';
		}
		if (obj.DetailFeld !== undefined) {
			result += '"DetailFeld" : ' + ((obj.DetailFeld === null) ? 'null' : JSON.stringify(obj.DetailFeld)) + ',';
		}
		if (obj.Reihenfolge !== undefined) {
			result += '"Reihenfolge" : ' + ((obj.Reihenfolge === null) ? 'null' : obj.Reihenfolge.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_Schild3KatalogEintragSchuelerImportExport(obj : unknown) : Schild3KatalogEintragSchuelerImportExport {
	return obj as Schild3KatalogEintragSchuelerImportExport;
}
