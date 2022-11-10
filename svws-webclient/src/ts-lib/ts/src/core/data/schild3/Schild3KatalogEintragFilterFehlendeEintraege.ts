import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragFilterFehlendeEintraege extends JavaObject {

	public ID : Number | null = null;

	public Beschreibung : String | null = null;

	public Feldname : String | null = null;

	public Tabellen : String | null = null;

	public SQLText : String | null = null;

	public Schulform : String | null = null;

	public Feldtyp : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragFilterFehlendeEintraege'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragFilterFehlendeEintraege {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragFilterFehlendeEintraege();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID === null ? null : Number(obj.ID);
		result.Beschreibung = typeof obj.Beschreibung === "undefined" ? null : obj.Beschreibung === null ? null : String(obj.Beschreibung);
		result.Feldname = typeof obj.Feldname === "undefined" ? null : obj.Feldname === null ? null : String(obj.Feldname);
		result.Tabellen = typeof obj.Tabellen === "undefined" ? null : obj.Tabellen === null ? null : String(obj.Tabellen);
		result.SQLText = typeof obj.SQLText === "undefined" ? null : obj.SQLText === null ? null : String(obj.SQLText);
		result.Schulform = typeof obj.Schulform === "undefined" ? null : obj.Schulform === null ? null : String(obj.Schulform);
		result.Feldtyp = typeof obj.Feldtyp === "undefined" ? null : obj.Feldtyp === null ? null : String(obj.Feldtyp);
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragFilterFehlendeEintraege) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		result += '"Beschreibung" : ' + ((!obj.Beschreibung) ? 'null' : '"' + obj.Beschreibung.valueOf() + '"') + ',';
		result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname.valueOf() + '"') + ',';
		result += '"Tabellen" : ' + ((!obj.Tabellen) ? 'null' : '"' + obj.Tabellen.valueOf() + '"') + ',';
		result += '"SQLText" : ' + ((!obj.SQLText) ? 'null' : '"' + obj.SQLText.valueOf() + '"') + ',';
		result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform.valueOf() + '"') + ',';
		result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragFilterFehlendeEintraege>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		}
		if (typeof obj.Beschreibung !== "undefined") {
			result += '"Beschreibung" : ' + ((!obj.Beschreibung) ? 'null' : '"' + obj.Beschreibung.valueOf() + '"') + ',';
		}
		if (typeof obj.Feldname !== "undefined") {
			result += '"Feldname" : ' + ((!obj.Feldname) ? 'null' : '"' + obj.Feldname.valueOf() + '"') + ',';
		}
		if (typeof obj.Tabellen !== "undefined") {
			result += '"Tabellen" : ' + ((!obj.Tabellen) ? 'null' : '"' + obj.Tabellen.valueOf() + '"') + ',';
		}
		if (typeof obj.SQLText !== "undefined") {
			result += '"SQLText" : ' + ((!obj.SQLText) ? 'null' : '"' + obj.SQLText.valueOf() + '"') + ',';
		}
		if (typeof obj.Schulform !== "undefined") {
			result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform.valueOf() + '"') + ',';
		}
		if (typeof obj.Feldtyp !== "undefined") {
			result += '"Feldtyp" : ' + ((!obj.Feldtyp) ? 'null' : '"' + obj.Feldtyp.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragFilterFehlendeEintraege(obj : unknown) : Schild3KatalogEintragFilterFehlendeEintraege {
	return obj as Schild3KatalogEintragFilterFehlendeEintraege;
}
