import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragLaender extends JavaObject {

	public Kurztext : String | null = null;

	public Langtext : String | null = null;

	public Sortierung : Number | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragLaender'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragLaender {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragLaender();
		result.Kurztext = typeof obj.Kurztext === "undefined" ? null : obj.Kurztext;
		result.Langtext = typeof obj.Langtext === "undefined" ? null : obj.Langtext;
		result.Sortierung = typeof obj.Sortierung === "undefined" ? null : obj.Sortierung;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragLaender) : string {
		let result = '{';
		result += '"Kurztext" : ' + ((!obj.Kurztext) ? 'null' : '"' + obj.Kurztext.valueOf() + '"') + ',';
		result += '"Langtext" : ' + ((!obj.Langtext) ? 'null' : '"' + obj.Langtext.valueOf() + '"') + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragLaender>) : string {
		let result = '{';
		if (typeof obj.Kurztext !== "undefined") {
			result += '"Kurztext" : ' + ((!obj.Kurztext) ? 'null' : '"' + obj.Kurztext.valueOf() + '"') + ',';
		}
		if (typeof obj.Langtext !== "undefined") {
			result += '"Langtext" : ' + ((!obj.Langtext) ? 'null' : '"' + obj.Langtext.valueOf() + '"') + ',';
		}
		if (typeof obj.Sortierung !== "undefined") {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
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

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragLaender(obj : unknown) : Schild3KatalogEintragLaender {
	return obj as Schild3KatalogEintragLaender;
}
