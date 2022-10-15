import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragFilterSpezial extends JavaObject {

	public ID : Number | null = null;

	public Gruppe : String | null = null;

	public KurzBez : String | null = null;

	public Bezeichnung : String | null = null;

	public Grundschule : String | null = null;

	public Tabelle : String | null = null;

	public DBFeld : String | null = null;

	public Typ : String | null = null;

	public Control : String | null = null;

	public WerteAnzeige : String | null = null;

	public WerteSQL : String | null = null;

	public LookupInfo : String | null = null;

	public OperatorenAnzeige : String | null = null;

	public OperatorenSQL : String | null = null;

	public Zusatzbedingung : String | null = null;

	public ZusatzTabellen : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragFilterSpezial'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragFilterSpezial {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragFilterSpezial();
		result.ID = typeof obj.ID === "undefined" ? null : obj.ID;
		result.Gruppe = typeof obj.Gruppe === "undefined" ? null : obj.Gruppe;
		result.KurzBez = typeof obj.KurzBez === "undefined" ? null : obj.KurzBez;
		result.Bezeichnung = typeof obj.Bezeichnung === "undefined" ? null : obj.Bezeichnung;
		result.Grundschule = typeof obj.Grundschule === "undefined" ? null : obj.Grundschule;
		result.Tabelle = typeof obj.Tabelle === "undefined" ? null : obj.Tabelle;
		result.DBFeld = typeof obj.DBFeld === "undefined" ? null : obj.DBFeld;
		result.Typ = typeof obj.Typ === "undefined" ? null : obj.Typ;
		result.Control = typeof obj.Control === "undefined" ? null : obj.Control;
		result.WerteAnzeige = typeof obj.WerteAnzeige === "undefined" ? null : obj.WerteAnzeige;
		result.WerteSQL = typeof obj.WerteSQL === "undefined" ? null : obj.WerteSQL;
		result.LookupInfo = typeof obj.LookupInfo === "undefined" ? null : obj.LookupInfo;
		result.OperatorenAnzeige = typeof obj.OperatorenAnzeige === "undefined" ? null : obj.OperatorenAnzeige;
		result.OperatorenSQL = typeof obj.OperatorenSQL === "undefined" ? null : obj.OperatorenSQL;
		result.Zusatzbedingung = typeof obj.Zusatzbedingung === "undefined" ? null : obj.Zusatzbedingung;
		result.ZusatzTabellen = typeof obj.ZusatzTabellen === "undefined" ? null : obj.ZusatzTabellen;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragFilterSpezial) : string {
		let result = '{';
		result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		result += '"Gruppe" : ' + ((!obj.Gruppe) ? 'null' : '"' + obj.Gruppe.valueOf() + '"') + ',';
		result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez.valueOf() + '"') + ',';
		result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung.valueOf() + '"') + ',';
		result += '"Grundschule" : ' + ((!obj.Grundschule) ? 'null' : '"' + obj.Grundschule.valueOf() + '"') + ',';
		result += '"Tabelle" : ' + ((!obj.Tabelle) ? 'null' : '"' + obj.Tabelle.valueOf() + '"') + ',';
		result += '"DBFeld" : ' + ((!obj.DBFeld) ? 'null' : '"' + obj.DBFeld.valueOf() + '"') + ',';
		result += '"Typ" : ' + ((!obj.Typ) ? 'null' : '"' + obj.Typ.valueOf() + '"') + ',';
		result += '"Control" : ' + ((!obj.Control) ? 'null' : '"' + obj.Control.valueOf() + '"') + ',';
		result += '"WerteAnzeige" : ' + ((!obj.WerteAnzeige) ? 'null' : '"' + obj.WerteAnzeige.valueOf() + '"') + ',';
		result += '"WerteSQL" : ' + ((!obj.WerteSQL) ? 'null' : '"' + obj.WerteSQL.valueOf() + '"') + ',';
		result += '"LookupInfo" : ' + ((!obj.LookupInfo) ? 'null' : '"' + obj.LookupInfo.valueOf() + '"') + ',';
		result += '"OperatorenAnzeige" : ' + ((!obj.OperatorenAnzeige) ? 'null' : '"' + obj.OperatorenAnzeige.valueOf() + '"') + ',';
		result += '"OperatorenSQL" : ' + ((!obj.OperatorenSQL) ? 'null' : '"' + obj.OperatorenSQL.valueOf() + '"') + ',';
		result += '"Zusatzbedingung" : ' + ((!obj.Zusatzbedingung) ? 'null' : '"' + obj.Zusatzbedingung.valueOf() + '"') + ',';
		result += '"ZusatzTabellen" : ' + ((!obj.ZusatzTabellen) ? 'null' : '"' + obj.ZusatzTabellen.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragFilterSpezial>) : string {
		let result = '{';
		if (typeof obj.ID !== "undefined") {
			result += '"ID" : ' + ((!obj.ID) ? 'null' : obj.ID.valueOf()) + ',';
		}
		if (typeof obj.Gruppe !== "undefined") {
			result += '"Gruppe" : ' + ((!obj.Gruppe) ? 'null' : '"' + obj.Gruppe.valueOf() + '"') + ',';
		}
		if (typeof obj.KurzBez !== "undefined") {
			result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez.valueOf() + '"') + ',';
		}
		if (typeof obj.Bezeichnung !== "undefined") {
			result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.Grundschule !== "undefined") {
			result += '"Grundschule" : ' + ((!obj.Grundschule) ? 'null' : '"' + obj.Grundschule.valueOf() + '"') + ',';
		}
		if (typeof obj.Tabelle !== "undefined") {
			result += '"Tabelle" : ' + ((!obj.Tabelle) ? 'null' : '"' + obj.Tabelle.valueOf() + '"') + ',';
		}
		if (typeof obj.DBFeld !== "undefined") {
			result += '"DBFeld" : ' + ((!obj.DBFeld) ? 'null' : '"' + obj.DBFeld.valueOf() + '"') + ',';
		}
		if (typeof obj.Typ !== "undefined") {
			result += '"Typ" : ' + ((!obj.Typ) ? 'null' : '"' + obj.Typ.valueOf() + '"') + ',';
		}
		if (typeof obj.Control !== "undefined") {
			result += '"Control" : ' + ((!obj.Control) ? 'null' : '"' + obj.Control.valueOf() + '"') + ',';
		}
		if (typeof obj.WerteAnzeige !== "undefined") {
			result += '"WerteAnzeige" : ' + ((!obj.WerteAnzeige) ? 'null' : '"' + obj.WerteAnzeige.valueOf() + '"') + ',';
		}
		if (typeof obj.WerteSQL !== "undefined") {
			result += '"WerteSQL" : ' + ((!obj.WerteSQL) ? 'null' : '"' + obj.WerteSQL.valueOf() + '"') + ',';
		}
		if (typeof obj.LookupInfo !== "undefined") {
			result += '"LookupInfo" : ' + ((!obj.LookupInfo) ? 'null' : '"' + obj.LookupInfo.valueOf() + '"') + ',';
		}
		if (typeof obj.OperatorenAnzeige !== "undefined") {
			result += '"OperatorenAnzeige" : ' + ((!obj.OperatorenAnzeige) ? 'null' : '"' + obj.OperatorenAnzeige.valueOf() + '"') + ',';
		}
		if (typeof obj.OperatorenSQL !== "undefined") {
			result += '"OperatorenSQL" : ' + ((!obj.OperatorenSQL) ? 'null' : '"' + obj.OperatorenSQL.valueOf() + '"') + ',';
		}
		if (typeof obj.Zusatzbedingung !== "undefined") {
			result += '"Zusatzbedingung" : ' + ((!obj.Zusatzbedingung) ? 'null' : '"' + obj.Zusatzbedingung.valueOf() + '"') + ',';
		}
		if (typeof obj.ZusatzTabellen !== "undefined") {
			result += '"ZusatzTabellen" : ' + ((!obj.ZusatzTabellen) ? 'null' : '"' + obj.ZusatzTabellen.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragFilterSpezial(obj : unknown) : Schild3KatalogEintragFilterSpezial {
	return obj as Schild3KatalogEintragFilterSpezial;
}
