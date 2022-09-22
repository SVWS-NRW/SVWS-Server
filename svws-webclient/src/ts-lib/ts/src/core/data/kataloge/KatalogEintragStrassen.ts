import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class KatalogEintragStrassen extends JavaObject {

	public Ort : String | null = null;

	public RegSchl : String | null = null;

	public Strasse : String | null = null;

	public Stand : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kataloge.KatalogEintragStrassen'].includes(name);
	}

	public static transpilerFromJSON(json : string): KatalogEintragStrassen {
		const obj = JSON.parse(json);
		const result = new KatalogEintragStrassen();
		result.Ort = typeof obj.Ort === "undefined" ? null : obj.Ort;
		result.RegSchl = typeof obj.RegSchl === "undefined" ? null : obj.RegSchl;
		result.Strasse = typeof obj.Strasse === "undefined" ? null : obj.Strasse;
		result.Stand = typeof obj.Stand === "undefined" ? null : obj.Stand;
		return result;
	}

	public static transpilerToJSON(obj : KatalogEintragStrassen) : string {
		let result = '{';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort.valueOf() + '"') + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse.valueOf() + '"') + ',';
		result += '"Stand" : ' + ((!obj.Stand) ? 'null' : '"' + obj.Stand.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KatalogEintragStrassen>) : string {
		let result = '{';
		if (typeof obj.Ort !== "undefined") {
			result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort.valueOf() + '"') + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		}
		if (typeof obj.Strasse !== "undefined") {
			result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse.valueOf() + '"') + ',';
		}
		if (typeof obj.Stand !== "undefined") {
			result += '"Stand" : ' + ((!obj.Stand) ? 'null' : '"' + obj.Stand.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kataloge_KatalogEintragStrassen(obj : unknown) : KatalogEintragStrassen {
	return obj as KatalogEintragStrassen;
}
