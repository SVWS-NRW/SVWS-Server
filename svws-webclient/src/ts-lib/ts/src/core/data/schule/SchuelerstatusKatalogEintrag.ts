import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerstatusKatalogEintrag extends JavaObject {

	public StatusNr : Number | null = null;

	public Bezeichnung : String | null = null;

	public Sortierung : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.SchuelerstatusKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerstatusKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchuelerstatusKatalogEintrag();
		result.StatusNr = typeof obj.StatusNr === "undefined" ? null : obj.StatusNr;
		result.Bezeichnung = typeof obj.Bezeichnung === "undefined" ? null : obj.Bezeichnung;
		result.Sortierung = typeof obj.Sortierung === "undefined" ? null : obj.Sortierung;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerstatusKatalogEintrag) : string {
		let result = '{';
		result += '"StatusNr" : ' + ((!obj.StatusNr) ? 'null' : obj.StatusNr.valueOf()) + ',';
		result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung.valueOf() + '"') + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerstatusKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.StatusNr !== "undefined") {
			result += '"StatusNr" : ' + ((!obj.StatusNr) ? 'null' : obj.StatusNr.valueOf()) + ',';
		}
		if (typeof obj.Bezeichnung !== "undefined") {
			result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.Sortierung !== "undefined") {
			result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_SchuelerstatusKatalogEintrag(obj : unknown) : SchuelerstatusKatalogEintrag {
	return obj as SchuelerstatusKatalogEintrag;
}
