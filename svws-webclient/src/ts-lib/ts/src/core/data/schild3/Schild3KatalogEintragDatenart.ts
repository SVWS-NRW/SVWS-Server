import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragDatenart extends JavaObject {

	public DatenartKrz : String | null = null;

	public Datenart : String | null = null;

	public Tabellenname : String | null = null;

	public Reihenfolge : Number | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragDatenart'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragDatenart {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragDatenart();
		result.DatenartKrz = typeof obj.DatenartKrz === "undefined" ? null : obj.DatenartKrz;
		result.Datenart = typeof obj.Datenart === "undefined" ? null : obj.Datenart;
		result.Tabellenname = typeof obj.Tabellenname === "undefined" ? null : obj.Tabellenname;
		result.Reihenfolge = typeof obj.Reihenfolge === "undefined" ? null : obj.Reihenfolge;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragDatenart) : string {
		let result = '{';
		result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : '"' + obj.DatenartKrz.valueOf() + '"') + ',';
		result += '"Datenart" : ' + ((!obj.Datenart) ? 'null' : '"' + obj.Datenart.valueOf() + '"') + ',';
		result += '"Tabellenname" : ' + ((!obj.Tabellenname) ? 'null' : '"' + obj.Tabellenname.valueOf() + '"') + ',';
		result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge.valueOf()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragDatenart>) : string {
		let result = '{';
		if (typeof obj.DatenartKrz !== "undefined") {
			result += '"DatenartKrz" : ' + ((!obj.DatenartKrz) ? 'null' : '"' + obj.DatenartKrz.valueOf() + '"') + ',';
		}
		if (typeof obj.Datenart !== "undefined") {
			result += '"Datenart" : ' + ((!obj.Datenart) ? 'null' : '"' + obj.Datenart.valueOf() + '"') + ',';
		}
		if (typeof obj.Tabellenname !== "undefined") {
			result += '"Tabellenname" : ' + ((!obj.Tabellenname) ? 'null' : '"' + obj.Tabellenname.valueOf() + '"') + ',';
		}
		if (typeof obj.Reihenfolge !== "undefined") {
			result += '"Reihenfolge" : ' + ((!obj.Reihenfolge) ? 'null' : obj.Reihenfolge.valueOf()) + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragDatenart(obj : unknown) : Schild3KatalogEintragDatenart {
	return obj as Schild3KatalogEintragDatenart;
}
