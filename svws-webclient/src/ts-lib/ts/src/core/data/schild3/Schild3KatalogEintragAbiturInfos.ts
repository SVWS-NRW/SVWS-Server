import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragAbiturInfos extends JavaObject {

	public PrfOrdnung : String | null = null;

	public AbiFach : String | null = null;

	public Bedingung : String | null = null;

	public AbiInfoKrz : String | null = null;

	public AbiInfoBeschreibung : String | null = null;

	public AbiInfoText : String | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragAbiturInfos'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragAbiturInfos {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragAbiturInfos();
		result.PrfOrdnung = typeof obj.PrfOrdnung === "undefined" ? null : obj.PrfOrdnung;
		result.AbiFach = typeof obj.AbiFach === "undefined" ? null : obj.AbiFach;
		result.Bedingung = typeof obj.Bedingung === "undefined" ? null : obj.Bedingung;
		result.AbiInfoKrz = typeof obj.AbiInfoKrz === "undefined" ? null : obj.AbiInfoKrz;
		result.AbiInfoBeschreibung = typeof obj.AbiInfoBeschreibung === "undefined" ? null : obj.AbiInfoBeschreibung;
		result.AbiInfoText = typeof obj.AbiInfoText === "undefined" ? null : obj.AbiInfoText;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragAbiturInfos) : string {
		let result = '{';
		result += '"PrfOrdnung" : ' + ((!obj.PrfOrdnung) ? 'null' : '"' + obj.PrfOrdnung.valueOf() + '"') + ',';
		result += '"AbiFach" : ' + ((!obj.AbiFach) ? 'null' : '"' + obj.AbiFach.valueOf() + '"') + ',';
		result += '"Bedingung" : ' + ((!obj.Bedingung) ? 'null' : '"' + obj.Bedingung.valueOf() + '"') + ',';
		result += '"AbiInfoKrz" : ' + ((!obj.AbiInfoKrz) ? 'null' : '"' + obj.AbiInfoKrz.valueOf() + '"') + ',';
		result += '"AbiInfoBeschreibung" : ' + ((!obj.AbiInfoBeschreibung) ? 'null' : '"' + obj.AbiInfoBeschreibung.valueOf() + '"') + ',';
		result += '"AbiInfoText" : ' + ((!obj.AbiInfoText) ? 'null' : '"' + obj.AbiInfoText.valueOf() + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragAbiturInfos>) : string {
		let result = '{';
		if (typeof obj.PrfOrdnung !== "undefined") {
			result += '"PrfOrdnung" : ' + ((!obj.PrfOrdnung) ? 'null' : '"' + obj.PrfOrdnung.valueOf() + '"') + ',';
		}
		if (typeof obj.AbiFach !== "undefined") {
			result += '"AbiFach" : ' + ((!obj.AbiFach) ? 'null' : '"' + obj.AbiFach.valueOf() + '"') + ',';
		}
		if (typeof obj.Bedingung !== "undefined") {
			result += '"Bedingung" : ' + ((!obj.Bedingung) ? 'null' : '"' + obj.Bedingung.valueOf() + '"') + ',';
		}
		if (typeof obj.AbiInfoKrz !== "undefined") {
			result += '"AbiInfoKrz" : ' + ((!obj.AbiInfoKrz) ? 'null' : '"' + obj.AbiInfoKrz.valueOf() + '"') + ',';
		}
		if (typeof obj.AbiInfoBeschreibung !== "undefined") {
			result += '"AbiInfoBeschreibung" : ' + ((!obj.AbiInfoBeschreibung) ? 'null' : '"' + obj.AbiInfoBeschreibung.valueOf() + '"') + ',';
		}
		if (typeof obj.AbiInfoText !== "undefined") {
			result += '"AbiInfoText" : ' + ((!obj.AbiInfoText) ? 'null' : '"' + obj.AbiInfoText.valueOf() + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragAbiturInfos(obj : unknown) : Schild3KatalogEintragAbiturInfos {
	return obj as Schild3KatalogEintragAbiturInfos;
}
