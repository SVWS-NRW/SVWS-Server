import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostBeratungslehrer, cast_de_nrw_schule_svws_core_data_gost_GostBeratungslehrer } from '../../../core/data/gost/GostBeratungslehrer';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class GostJahrgangsdaten extends JavaObject {

	public abiturjahr : Number | null = null;

	public jahrgang : String | null = null;

	public bezeichnung : String | null = null;

	public istAbgeschlossen : boolean = false;

	public textBeratungsbogen : String | null = null;

	public textMailversand : String | null = null;

	public beginnZusatzkursGE : String | null = null;

	public beginnZusatzkursSW : String | null = null;

	public readonly beratungslehrer : Vector<GostBeratungslehrer> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostJahrgangsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostJahrgangsdaten {
		const obj = JSON.parse(json);
		const result = new GostJahrgangsdaten();
		result.abiturjahr = typeof obj.abiturjahr === "undefined" ? null : obj.abiturjahr;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung;
		if (typeof obj.istAbgeschlossen === "undefined")
			 throw new Error('invalid json format, missing attribute istAbgeschlossen');
		result.istAbgeschlossen = obj.istAbgeschlossen;
		result.textBeratungsbogen = typeof obj.textBeratungsbogen === "undefined" ? null : obj.textBeratungsbogen;
		result.textMailversand = typeof obj.textMailversand === "undefined" ? null : obj.textMailversand;
		result.beginnZusatzkursGE = typeof obj.beginnZusatzkursGE === "undefined" ? null : obj.beginnZusatzkursGE;
		result.beginnZusatzkursSW = typeof obj.beginnZusatzkursSW === "undefined" ? null : obj.beginnZusatzkursSW;
		if (!!obj.beratungslehrer) {
			for (let elem of obj.beratungslehrer) {
				result.beratungslehrer?.add(GostBeratungslehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgangsdaten) : string {
		let result = '{';
		result += '"abiturjahr" : ' + ((!obj.abiturjahr) ? 'null' : obj.abiturjahr.valueOf()) + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		result += '"textBeratungsbogen" : ' + ((!obj.textBeratungsbogen) ? 'null' : '"' + obj.textBeratungsbogen.valueOf() + '"') + ',';
		result += '"textMailversand" : ' + ((!obj.textMailversand) ? 'null' : '"' + obj.textMailversand.valueOf() + '"') + ',';
		result += '"beginnZusatzkursGE" : ' + ((!obj.beginnZusatzkursGE) ? 'null' : '"' + obj.beginnZusatzkursGE.valueOf() + '"') + ',';
		result += '"beginnZusatzkursSW" : ' + ((!obj.beginnZusatzkursSW) ? 'null' : '"' + obj.beginnZusatzkursSW.valueOf() + '"') + ',';
		if (!obj.beratungslehrer) {
			result += '"beratungslehrer" : []';
		} else {
			result += '"beratungslehrer" : [ ';
			for (let i : number = 0; i < obj.beratungslehrer.size(); i++) {
				let elem = obj.beratungslehrer.get(i);
				result += GostBeratungslehrer.transpilerToJSON(elem);
				if (i < obj.beratungslehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgangsdaten>) : string {
		let result = '{';
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + ((!obj.abiturjahr) ? 'null' : obj.abiturjahr.valueOf()) + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.istAbgeschlossen !== "undefined") {
			result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		}
		if (typeof obj.textBeratungsbogen !== "undefined") {
			result += '"textBeratungsbogen" : ' + ((!obj.textBeratungsbogen) ? 'null' : '"' + obj.textBeratungsbogen.valueOf() + '"') + ',';
		}
		if (typeof obj.textMailversand !== "undefined") {
			result += '"textMailversand" : ' + ((!obj.textMailversand) ? 'null' : '"' + obj.textMailversand.valueOf() + '"') + ',';
		}
		if (typeof obj.beginnZusatzkursGE !== "undefined") {
			result += '"beginnZusatzkursGE" : ' + ((!obj.beginnZusatzkursGE) ? 'null' : '"' + obj.beginnZusatzkursGE.valueOf() + '"') + ',';
		}
		if (typeof obj.beginnZusatzkursSW !== "undefined") {
			result += '"beginnZusatzkursSW" : ' + ((!obj.beginnZusatzkursSW) ? 'null' : '"' + obj.beginnZusatzkursSW.valueOf() + '"') + ',';
		}
		if (typeof obj.beratungslehrer !== "undefined") {
			if (!obj.beratungslehrer) {
				result += '"beratungslehrer" : []';
			} else {
				result += '"beratungslehrer" : [ ';
				for (let i : number = 0; i < obj.beratungslehrer.size(); i++) {
					let elem = obj.beratungslehrer.get(i);
					result += GostBeratungslehrer.transpilerToJSON(elem);
					if (i < obj.beratungslehrer.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostJahrgangsdaten(obj : unknown) : GostJahrgangsdaten {
	return obj as GostJahrgangsdaten;
}
