import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

export class Schild3KatalogEintragSchuelerStatus extends JavaObject {

	public StatusNr : Number | null = null;

	public Bezeichnung : String | null = null;

	public Sortierung : Number | null = null;

	public InSimExp : Boolean | null = null;

	public SIMAbschnitt : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragSchuelerStatus'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragSchuelerStatus {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragSchuelerStatus();
		result.StatusNr = typeof obj.StatusNr === "undefined" ? null : obj.StatusNr;
		result.Bezeichnung = typeof obj.Bezeichnung === "undefined" ? null : obj.Bezeichnung;
		result.Sortierung = typeof obj.Sortierung === "undefined" ? null : obj.Sortierung;
		result.InSimExp = typeof obj.InSimExp === "undefined" ? null : obj.InSimExp;
		result.SIMAbschnitt = typeof obj.SIMAbschnitt === "undefined" ? null : obj.SIMAbschnitt;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragSchuelerStatus) : string {
		let result = '{';
		result += '"StatusNr" : ' + ((!obj.StatusNr) ? 'null' : obj.StatusNr.valueOf()) + ',';
		result += '"Bezeichnung" : ' + ((!obj.Bezeichnung) ? 'null' : '"' + obj.Bezeichnung.valueOf() + '"') + ',';
		result += '"Sortierung" : ' + ((!obj.Sortierung) ? 'null' : obj.Sortierung.valueOf()) + ',';
		result += '"InSimExp" : ' + ((!obj.InSimExp) ? 'null' : obj.InSimExp.valueOf()) + ',';
		result += '"SIMAbschnitt" : ' + ((!obj.SIMAbschnitt) ? 'null' : '"' + obj.SIMAbschnitt.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragSchuelerStatus>) : string {
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
		if (typeof obj.InSimExp !== "undefined") {
			result += '"InSimExp" : ' + ((!obj.InSimExp) ? 'null' : obj.InSimExp.valueOf()) + ',';
		}
		if (typeof obj.SIMAbschnitt !== "undefined") {
			result += '"SIMAbschnitt" : ' + ((!obj.SIMAbschnitt) ? 'null' : '"' + obj.SIMAbschnitt.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragSchuelerStatus(obj : unknown) : Schild3KatalogEintragSchuelerStatus {
	return obj as Schild3KatalogEintragSchuelerStatus;
}
