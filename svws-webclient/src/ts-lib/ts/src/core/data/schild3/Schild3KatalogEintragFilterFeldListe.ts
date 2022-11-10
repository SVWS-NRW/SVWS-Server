import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragFilterFeldListe extends JavaObject {

	public ID : Number | null = null;

	public Bezeichnung : String | null = null;

	public DBFeld : String | null = null;

	public Typ : String | null = null;

	public Werte : String | null = null;

	public StdWert : String | null = null;

	public Operator : String | null = null;

	public Zusatzbedingung : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragFilterFeldListe'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragFilterFeldListe {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragFilterFeldListe();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID === null ? null : Number(obj.ID);
		result.Bezeichnung = typeof obj.Bezeichnung === "undefined" ? null : obj.Bezeichnung === null ? null : String(obj.Bezeichnung);
		result.DBFeld = typeof obj.DBFeld === "undefined" ? null : obj.DBFeld === null ? null : String(obj.DBFeld);
		result.Typ = typeof obj.Typ === "undefined" ? null : obj.Typ === null ? null : String(obj.Typ);
		result.Werte = typeof obj.Werte === "undefined" ? null : obj.Werte === null ? null : String(obj.Werte);
		result.StdWert = typeof obj.StdWert === "undefined" ? null : obj.StdWert === null ? null : String(obj.StdWert);
		result.Operator = typeof obj.Operator === "undefined" ? null : obj.Operator === null ? null : String(obj.Operator);
		result.Zusatzbedingung = typeof obj.Zusatzbedingung === "undefined" ? null : obj.Zusatzbedingung === null ? null : String(obj.Zusatzbedingung);
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragFilterFeldListe) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung.valueOf() + '"') + ',';
		result += '"DBFeld" : ' + ((!obj.DBFeld) ? 'null' : '"' + obj.DBFeld.valueOf() + '"') + ',';
		result += '"Typ" : ' + ((!obj.Typ) ? 'null' : '"' + obj.Typ.valueOf() + '"') + ',';
		result += '"Werte" : ' + ((!obj.Werte) ? 'null' : '"' + obj.Werte.valueOf() + '"') + ',';
		result += '"StdWert" : ' + ((!obj.StdWert) ? 'null' : '"' + obj.StdWert.valueOf() + '"') + ',';
		result += '"Operator" : ' + ((!obj.Operator) ? 'null' : '"' + obj.Operator.valueOf() + '"') + ',';
		result += '"Zusatzbedingung" : ' + ((!obj.Zusatzbedingung) ? 'null' : '"' + obj.Zusatzbedingung.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragFilterFeldListe>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		}
		if (typeof obj.Bezeichnung !== "undefined") {
			result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.DBFeld !== "undefined") {
			result += '"DBFeld" : ' + ((!obj.DBFeld) ? 'null' : '"' + obj.DBFeld.valueOf() + '"') + ',';
		}
		if (typeof obj.Typ !== "undefined") {
			result += '"Typ" : ' + ((!obj.Typ) ? 'null' : '"' + obj.Typ.valueOf() + '"') + ',';
		}
		if (typeof obj.Werte !== "undefined") {
			result += '"Werte" : ' + ((!obj.Werte) ? 'null' : '"' + obj.Werte.valueOf() + '"') + ',';
		}
		if (typeof obj.StdWert !== "undefined") {
			result += '"StdWert" : ' + ((!obj.StdWert) ? 'null' : '"' + obj.StdWert.valueOf() + '"') + ',';
		}
		if (typeof obj.Operator !== "undefined") {
			result += '"Operator" : ' + ((!obj.Operator) ? 'null' : '"' + obj.Operator.valueOf() + '"') + ',';
		}
		if (typeof obj.Zusatzbedingung !== "undefined") {
			result += '"Zusatzbedingung" : ' + ((!obj.Zusatzbedingung) ? 'null' : '"' + obj.Zusatzbedingung.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragFilterFeldListe(obj : unknown) : Schild3KatalogEintragFilterFeldListe {
	return obj as Schild3KatalogEintragFilterFeldListe;
}
