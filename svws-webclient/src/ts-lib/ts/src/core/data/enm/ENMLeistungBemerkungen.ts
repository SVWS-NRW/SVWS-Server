import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMLeistungBemerkungen extends JavaObject {

	public ASV : String | null = null;

	public AUE : String | null = null;

	public ZB : String | null = null;

	public LELS : String | null = null;

	public schulformEmpf : String | null = null;

	public individuelleVersetzungsbemerkungen : String | null = null;

	public foerderbemerkungen : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMLeistungBemerkungen'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLeistungBemerkungen {
		const obj = JSON.parse(json);
		const result = new ENMLeistungBemerkungen();
		result.ASV = typeof obj.ASV === "undefined" ? null : obj.ASV;
		result.AUE = typeof obj.AUE === "undefined" ? null : obj.AUE;
		result.ZB = typeof obj.ZB === "undefined" ? null : obj.ZB;
		result.LELS = typeof obj.LELS === "undefined" ? null : obj.LELS;
		result.schulformEmpf = typeof obj.schulformEmpf === "undefined" ? null : obj.schulformEmpf;
		result.individuelleVersetzungsbemerkungen = typeof obj.individuelleVersetzungsbemerkungen === "undefined" ? null : obj.individuelleVersetzungsbemerkungen;
		result.foerderbemerkungen = typeof obj.foerderbemerkungen === "undefined" ? null : obj.foerderbemerkungen;
		return result;
	}

	public static transpilerToJSON(obj : ENMLeistungBemerkungen) : string {
		let result = '{';
		result += '"ASV" : ' + ((!obj.ASV) ? 'null' : '"' + obj.ASV.valueOf() + '"') + ',';
		result += '"AUE" : ' + ((!obj.AUE) ? 'null' : '"' + obj.AUE.valueOf() + '"') + ',';
		result += '"ZB" : ' + ((!obj.ZB) ? 'null' : '"' + obj.ZB.valueOf() + '"') + ',';
		result += '"LELS" : ' + ((!obj.LELS) ? 'null' : '"' + obj.LELS.valueOf() + '"') + ',';
		result += '"schulformEmpf" : ' + ((!obj.schulformEmpf) ? 'null' : '"' + obj.schulformEmpf.valueOf() + '"') + ',';
		result += '"individuelleVersetzungsbemerkungen" : ' + ((!obj.individuelleVersetzungsbemerkungen) ? 'null' : '"' + obj.individuelleVersetzungsbemerkungen.valueOf() + '"') + ',';
		result += '"foerderbemerkungen" : ' + ((!obj.foerderbemerkungen) ? 'null' : '"' + obj.foerderbemerkungen.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLeistungBemerkungen>) : string {
		let result = '{';
		if (typeof obj.ASV !== "undefined") {
			result += '"ASV" : ' + ((!obj.ASV) ? 'null' : '"' + obj.ASV.valueOf() + '"') + ',';
		}
		if (typeof obj.AUE !== "undefined") {
			result += '"AUE" : ' + ((!obj.AUE) ? 'null' : '"' + obj.AUE.valueOf() + '"') + ',';
		}
		if (typeof obj.ZB !== "undefined") {
			result += '"ZB" : ' + ((!obj.ZB) ? 'null' : '"' + obj.ZB.valueOf() + '"') + ',';
		}
		if (typeof obj.LELS !== "undefined") {
			result += '"LELS" : ' + ((!obj.LELS) ? 'null' : '"' + obj.LELS.valueOf() + '"') + ',';
		}
		if (typeof obj.schulformEmpf !== "undefined") {
			result += '"schulformEmpf" : ' + ((!obj.schulformEmpf) ? 'null' : '"' + obj.schulformEmpf.valueOf() + '"') + ',';
		}
		if (typeof obj.individuelleVersetzungsbemerkungen !== "undefined") {
			result += '"individuelleVersetzungsbemerkungen" : ' + ((!obj.individuelleVersetzungsbemerkungen) ? 'null' : '"' + obj.individuelleVersetzungsbemerkungen.valueOf() + '"') + ',';
		}
		if (typeof obj.foerderbemerkungen !== "undefined") {
			result += '"foerderbemerkungen" : ' + ((!obj.foerderbemerkungen) ? 'null' : '"' + obj.foerderbemerkungen.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMLeistungBemerkungen(obj : unknown) : ENMLeistungBemerkungen {
	return obj as ENMLeistungBemerkungen;
}
