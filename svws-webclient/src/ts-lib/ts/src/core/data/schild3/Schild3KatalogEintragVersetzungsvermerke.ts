import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragVersetzungsvermerke extends JavaObject {

	public Nr : String | null = null;

	public Klartext : String | null = null;

	public StatistikKrz : String | null = null;

	public Sortierung : Number | null = null;

	public Schulform : String | null = null;

	public StatistikKrzNeu : String | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragVersetzungsvermerke'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragVersetzungsvermerke {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragVersetzungsvermerke();
		result.Nr = typeof obj.Nr === "undefined" ? null : obj.Nr;
		result.Klartext = typeof obj.Klartext === "undefined" ? null : obj.Klartext;
		result.StatistikKrz = typeof obj.StatistikKrz === "undefined" ? null : obj.StatistikKrz;
		result.Sortierung = typeof obj.Sortierung === "undefined" ? null : obj.Sortierung;
		result.Schulform = typeof obj.Schulform === "undefined" ? null : obj.Schulform;
		result.StatistikKrzNeu = typeof obj.StatistikKrzNeu === "undefined" ? null : obj.StatistikKrzNeu;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragVersetzungsvermerke) : string {
		let result = '{';
		result += '"Nr" : ' + ((!obj.Nr) ? 'null' : '"' + obj.Nr.valueOf() + '"') + ',';
		result += '"Klartext" : ' + ((!obj.Klartext) ? 'null' : '"' + obj.Klartext.valueOf() + '"') + ',';
		result += '"StatistikKrz" : ' + ((!obj.StatistikKrz) ? 'null' : '"' + obj.StatistikKrz.valueOf() + '"') + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform.valueOf() + '"') + ',';
		result += '"StatistikKrzNeu" : ' + ((!obj.StatistikKrzNeu) ? 'null' : '"' + obj.StatistikKrzNeu.valueOf() + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragVersetzungsvermerke>) : string {
		let result = '{';
		if (typeof obj.Nr !== "undefined") {
			result += '"Nr" : ' + ((!obj.Nr) ? 'null' : '"' + obj.Nr.valueOf() + '"') + ',';
		}
		if (typeof obj.Klartext !== "undefined") {
			result += '"Klartext" : ' + ((!obj.Klartext) ? 'null' : '"' + obj.Klartext.valueOf() + '"') + ',';
		}
		if (typeof obj.StatistikKrz !== "undefined") {
			result += '"StatistikKrz" : ' + ((!obj.StatistikKrz) ? 'null' : '"' + obj.StatistikKrz.valueOf() + '"') + ',';
		}
		if (typeof obj.Sortierung !== "undefined") {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		}
		if (typeof obj.Schulform !== "undefined") {
			result += '"Schulform" : ' + ((!obj.Schulform) ? 'null' : '"' + obj.Schulform.valueOf() + '"') + ',';
		}
		if (typeof obj.StatistikKrzNeu !== "undefined") {
			result += '"StatistikKrzNeu" : ' + ((!obj.StatistikKrzNeu) ? 'null' : '"' + obj.StatistikKrzNeu.valueOf() + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragVersetzungsvermerke(obj : unknown) : Schild3KatalogEintragVersetzungsvermerke {
	return obj as Schild3KatalogEintragVersetzungsvermerke;
}
