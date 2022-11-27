import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMLeistungBemerkungen extends JavaObject {

	public ASV : String | null = null;

	public tsASV : String | null = null;

	public AUE : String | null = null;

	public tsAUE : String | null = null;

	public ZB : String | null = null;

	public tsZB : String | null = null;

	public LELS : String | null = null;

	public schulformEmpf : String | null = null;

	public individuelleVersetzungsbemerkungen : String | null = null;

	public tsIndividuelleVersetzungsbemerkungen : String | null = null;

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
		result.ASV = typeof obj.ASV === "undefined" ? null : obj.ASV === null ? null : String(obj.ASV);
		result.tsASV = typeof obj.tsASV === "undefined" ? null : obj.tsASV === null ? null : String(obj.tsASV);
		result.AUE = typeof obj.AUE === "undefined" ? null : obj.AUE === null ? null : String(obj.AUE);
		result.tsAUE = typeof obj.tsAUE === "undefined" ? null : obj.tsAUE === null ? null : String(obj.tsAUE);
		result.ZB = typeof obj.ZB === "undefined" ? null : obj.ZB === null ? null : String(obj.ZB);
		result.tsZB = typeof obj.tsZB === "undefined" ? null : obj.tsZB === null ? null : String(obj.tsZB);
		result.LELS = typeof obj.LELS === "undefined" ? null : obj.LELS === null ? null : String(obj.LELS);
		result.schulformEmpf = typeof obj.schulformEmpf === "undefined" ? null : obj.schulformEmpf === null ? null : String(obj.schulformEmpf);
		result.individuelleVersetzungsbemerkungen = typeof obj.individuelleVersetzungsbemerkungen === "undefined" ? null : obj.individuelleVersetzungsbemerkungen === null ? null : String(obj.individuelleVersetzungsbemerkungen);
		result.tsIndividuelleVersetzungsbemerkungen = typeof obj.tsIndividuelleVersetzungsbemerkungen === "undefined" ? null : obj.tsIndividuelleVersetzungsbemerkungen === null ? null : String(obj.tsIndividuelleVersetzungsbemerkungen);
		result.foerderbemerkungen = typeof obj.foerderbemerkungen === "undefined" ? null : obj.foerderbemerkungen === null ? null : String(obj.foerderbemerkungen);
		return result;
	}

	public static transpilerToJSON(obj : ENMLeistungBemerkungen) : string {
		let result = '{';
		result += '"ASV" : ' + ((!obj.ASV) ? 'null' : '"' + obj.ASV.valueOf() + '"') + ',';
		result += '"tsASV" : ' + ((!obj.tsASV) ? 'null' : '"' + obj.tsASV.valueOf() + '"') + ',';
		result += '"AUE" : ' + ((!obj.AUE) ? 'null' : '"' + obj.AUE.valueOf() + '"') + ',';
		result += '"tsAUE" : ' + ((!obj.tsAUE) ? 'null' : '"' + obj.tsAUE.valueOf() + '"') + ',';
		result += '"ZB" : ' + ((!obj.ZB) ? 'null' : '"' + obj.ZB.valueOf() + '"') + ',';
		result += '"tsZB" : ' + ((!obj.tsZB) ? 'null' : '"' + obj.tsZB.valueOf() + '"') + ',';
		result += '"LELS" : ' + ((!obj.LELS) ? 'null' : '"' + obj.LELS.valueOf() + '"') + ',';
		result += '"schulformEmpf" : ' + ((!obj.schulformEmpf) ? 'null' : '"' + obj.schulformEmpf.valueOf() + '"') + ',';
		result += '"individuelleVersetzungsbemerkungen" : ' + ((!obj.individuelleVersetzungsbemerkungen) ? 'null' : '"' + obj.individuelleVersetzungsbemerkungen.valueOf() + '"') + ',';
		result += '"tsIndividuelleVersetzungsbemerkungen" : ' + ((!obj.tsIndividuelleVersetzungsbemerkungen) ? 'null' : '"' + obj.tsIndividuelleVersetzungsbemerkungen.valueOf() + '"') + ',';
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
		if (typeof obj.tsASV !== "undefined") {
			result += '"tsASV" : ' + ((!obj.tsASV) ? 'null' : '"' + obj.tsASV.valueOf() + '"') + ',';
		}
		if (typeof obj.AUE !== "undefined") {
			result += '"AUE" : ' + ((!obj.AUE) ? 'null' : '"' + obj.AUE.valueOf() + '"') + ',';
		}
		if (typeof obj.tsAUE !== "undefined") {
			result += '"tsAUE" : ' + ((!obj.tsAUE) ? 'null' : '"' + obj.tsAUE.valueOf() + '"') + ',';
		}
		if (typeof obj.ZB !== "undefined") {
			result += '"ZB" : ' + ((!obj.ZB) ? 'null' : '"' + obj.ZB.valueOf() + '"') + ',';
		}
		if (typeof obj.tsZB !== "undefined") {
			result += '"tsZB" : ' + ((!obj.tsZB) ? 'null' : '"' + obj.tsZB.valueOf() + '"') + ',';
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
		if (typeof obj.tsIndividuelleVersetzungsbemerkungen !== "undefined") {
			result += '"tsIndividuelleVersetzungsbemerkungen" : ' + ((!obj.tsIndividuelleVersetzungsbemerkungen) ? 'null' : '"' + obj.tsIndividuelleVersetzungsbemerkungen.valueOf() + '"') + ',';
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
