import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragFilterFehlendeEintraege extends JavaObject {

	/**
	 * ID des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden 
	 */
	public ID : number | null = null;

	/**
	 * Beschreibung des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden 
	 */
	public Beschreibung : string | null = null;

	/**
	 * Feldname des zu prüfenden Feldes 
	 */
	public Feldname : string | null = null;

	/**
	 * Tabellenname des zu prüfenden Feldes 
	 */
	public Tabellen : string | null = null;

	/**
	 * Abfrage die zur Prüfung des Feldes führt. 
	 */
	public SQLText : string | null = null;

	/**
	 * ggf. Schulform für bestimmte Schulformen 
	 */
	public Schulform : string | null = null;

	/**
	 * Feldtyp des zu prüfenden Feldes 
	 */
	public Feldtyp : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragFilterFehlendeEintraege'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragFilterFehlendeEintraege {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragFilterFehlendeEintraege();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID === null ? null : obj.ID;
		result.Beschreibung = typeof obj.Beschreibung === "undefined" ? null : obj.Beschreibung === null ? null : obj.Beschreibung;
		result.Feldname = typeof obj.Feldname === "undefined" ? null : obj.Feldname === null ? null : obj.Feldname;
		result.Tabellen = typeof obj.Tabellen === "undefined" ? null : obj.Tabellen === null ? null : obj.Tabellen;
		result.SQLText = typeof obj.SQLText === "undefined" ? null : obj.SQLText === null ? null : obj.SQLText;
		result.Schulform = typeof obj.Schulform === "undefined" ? null : obj.Schulform === null ? null : obj.Schulform;
		result.Feldtyp = typeof obj.Feldtyp === "undefined" ? null : obj.Feldtyp === null ? null : obj.Feldtyp;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragFilterFehlendeEintraege) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		result += '"Beschreibung" : ' + ((!obj.Beschreibung) ? 'null' : '"' + obj.Beschreibung + '"') + ',';
		result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname + '"') + ',';
		result += '"Tabellen" : ' + ((!obj.Tabellen) ? 'null' : '"' + obj.Tabellen + '"') + ',';
		result += '"SQLText" : ' + ((!obj.SQLText) ? 'null' : '"' + obj.SQLText + '"') + ',';
		result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform + '"') + ',';
		result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragFilterFehlendeEintraege>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID) + ',';
		}
		if (typeof obj.Beschreibung !== "undefined") {
			result += '"Beschreibung" : ' + ((!obj.Beschreibung) ? 'null' : '"' + obj.Beschreibung + '"') + ',';
		}
		if (typeof obj.Feldname !== "undefined") {
			result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname + '"') + ',';
		}
		if (typeof obj.Tabellen !== "undefined") {
			result += '"Tabellen" : ' + ((!obj.Tabellen) ? 'null' : '"' + obj.Tabellen + '"') + ',';
		}
		if (typeof obj.SQLText !== "undefined") {
			result += '"SQLText" : ' + ((!obj.SQLText) ? 'null' : '"' + obj.SQLText + '"') + ',';
		}
		if (typeof obj.Schulform !== "undefined") {
			result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform + '"') + ',';
		}
		if (typeof obj.Feldtyp !== "undefined") {
			result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragFilterFehlendeEintraege(obj : unknown) : Schild3KatalogEintragFilterFehlendeEintraege {
	return obj as Schild3KatalogEintragFilterFehlendeEintraege;
}
