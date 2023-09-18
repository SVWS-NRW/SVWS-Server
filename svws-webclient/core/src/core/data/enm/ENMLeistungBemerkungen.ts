import { JavaObject } from '../../../java/lang/JavaObject';

export class ENMLeistungBemerkungen extends JavaObject {

	/**
	 * Informationen zum Arbeits- und Sozialverhalten
	 */
	public ASV : string | null = null;

	/**
	 * Der Zeitstempel mit den letzten Änderungen zu Bemerkungen zum Arbeits- und Sozialverhalten
	 */
	public tsASV : string | null = null;

	/**
	 * Informationen zu dem Außerunterrichtlichen Engagement (AUE)
	 */
	public AUE : string | null = null;

	/**
	 * Der Zeitstempel mit den letzten Änderungen zu Bemerkungen zum Außerunterrichtlichen Engagement (AUE)
	 */
	public tsAUE : string | null = null;

	/**
	 * Zeugnisbemerkungen
	 */
	public ZB : string | null = null;

	/**
	 * Der Zeitstempel mit den letzten Änderungen zu Zeugnis-Bemerkungen
	 */
	public tsZB : string | null = null;

	/**
	 * Bemerkungen zur Lern und Leistungsentwicklung (LELS) in den Fächern
	 */
	public LELS : string | null = null;

	/**
	 * Schulform-Empfehlungen
	 */
	public schulformEmpf : string | null = null;

	/**
	 * Individuelle Bemerkungen zur Versetzung
	 */
	public individuelleVersetzungsbemerkungen : string | null = null;

	/**
	 * Der Zeitstempel mit den letzten Änderungen zu individuellen Bemerkungen zur Versetzung
	 */
	public tsIndividuelleVersetzungsbemerkungen : string | null = null;

	/**
	 * Förderbemerkungen
	 */
	public foerderbemerkungen : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMLeistungBemerkungen'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLeistungBemerkungen {
		const obj = JSON.parse(json);
		const result = new ENMLeistungBemerkungen();
		result.ASV = typeof obj.ASV === "undefined" ? null : obj.ASV === null ? null : obj.ASV;
		result.tsASV = typeof obj.tsASV === "undefined" ? null : obj.tsASV === null ? null : obj.tsASV;
		result.AUE = typeof obj.AUE === "undefined" ? null : obj.AUE === null ? null : obj.AUE;
		result.tsAUE = typeof obj.tsAUE === "undefined" ? null : obj.tsAUE === null ? null : obj.tsAUE;
		result.ZB = typeof obj.ZB === "undefined" ? null : obj.ZB === null ? null : obj.ZB;
		result.tsZB = typeof obj.tsZB === "undefined" ? null : obj.tsZB === null ? null : obj.tsZB;
		result.LELS = typeof obj.LELS === "undefined" ? null : obj.LELS === null ? null : obj.LELS;
		result.schulformEmpf = typeof obj.schulformEmpf === "undefined" ? null : obj.schulformEmpf === null ? null : obj.schulformEmpf;
		result.individuelleVersetzungsbemerkungen = typeof obj.individuelleVersetzungsbemerkungen === "undefined" ? null : obj.individuelleVersetzungsbemerkungen === null ? null : obj.individuelleVersetzungsbemerkungen;
		result.tsIndividuelleVersetzungsbemerkungen = typeof obj.tsIndividuelleVersetzungsbemerkungen === "undefined" ? null : obj.tsIndividuelleVersetzungsbemerkungen === null ? null : obj.tsIndividuelleVersetzungsbemerkungen;
		result.foerderbemerkungen = typeof obj.foerderbemerkungen === "undefined" ? null : obj.foerderbemerkungen === null ? null : obj.foerderbemerkungen;
		return result;
	}

	public static transpilerToJSON(obj : ENMLeistungBemerkungen) : string {
		let result = '{';
		result += '"ASV" : ' + ((!obj.ASV) ? 'null' : JSON.stringify(obj.ASV)) + ',';
		result += '"tsASV" : ' + ((!obj.tsASV) ? 'null' : JSON.stringify(obj.tsASV)) + ',';
		result += '"AUE" : ' + ((!obj.AUE) ? 'null' : JSON.stringify(obj.AUE)) + ',';
		result += '"tsAUE" : ' + ((!obj.tsAUE) ? 'null' : JSON.stringify(obj.tsAUE)) + ',';
		result += '"ZB" : ' + ((!obj.ZB) ? 'null' : JSON.stringify(obj.ZB)) + ',';
		result += '"tsZB" : ' + ((!obj.tsZB) ? 'null' : JSON.stringify(obj.tsZB)) + ',';
		result += '"LELS" : ' + ((!obj.LELS) ? 'null' : JSON.stringify(obj.LELS)) + ',';
		result += '"schulformEmpf" : ' + ((!obj.schulformEmpf) ? 'null' : JSON.stringify(obj.schulformEmpf)) + ',';
		result += '"individuelleVersetzungsbemerkungen" : ' + ((!obj.individuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.individuelleVersetzungsbemerkungen)) + ',';
		result += '"tsIndividuelleVersetzungsbemerkungen" : ' + ((!obj.tsIndividuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.tsIndividuelleVersetzungsbemerkungen)) + ',';
		result += '"foerderbemerkungen" : ' + ((!obj.foerderbemerkungen) ? 'null' : JSON.stringify(obj.foerderbemerkungen)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLeistungBemerkungen>) : string {
		let result = '{';
		if (typeof obj.ASV !== "undefined") {
			result += '"ASV" : ' + ((!obj.ASV) ? 'null' : JSON.stringify(obj.ASV)) + ',';
		}
		if (typeof obj.tsASV !== "undefined") {
			result += '"tsASV" : ' + ((!obj.tsASV) ? 'null' : JSON.stringify(obj.tsASV)) + ',';
		}
		if (typeof obj.AUE !== "undefined") {
			result += '"AUE" : ' + ((!obj.AUE) ? 'null' : JSON.stringify(obj.AUE)) + ',';
		}
		if (typeof obj.tsAUE !== "undefined") {
			result += '"tsAUE" : ' + ((!obj.tsAUE) ? 'null' : JSON.stringify(obj.tsAUE)) + ',';
		}
		if (typeof obj.ZB !== "undefined") {
			result += '"ZB" : ' + ((!obj.ZB) ? 'null' : JSON.stringify(obj.ZB)) + ',';
		}
		if (typeof obj.tsZB !== "undefined") {
			result += '"tsZB" : ' + ((!obj.tsZB) ? 'null' : JSON.stringify(obj.tsZB)) + ',';
		}
		if (typeof obj.LELS !== "undefined") {
			result += '"LELS" : ' + ((!obj.LELS) ? 'null' : JSON.stringify(obj.LELS)) + ',';
		}
		if (typeof obj.schulformEmpf !== "undefined") {
			result += '"schulformEmpf" : ' + ((!obj.schulformEmpf) ? 'null' : JSON.stringify(obj.schulformEmpf)) + ',';
		}
		if (typeof obj.individuelleVersetzungsbemerkungen !== "undefined") {
			result += '"individuelleVersetzungsbemerkungen" : ' + ((!obj.individuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.individuelleVersetzungsbemerkungen)) + ',';
		}
		if (typeof obj.tsIndividuelleVersetzungsbemerkungen !== "undefined") {
			result += '"tsIndividuelleVersetzungsbemerkungen" : ' + ((!obj.tsIndividuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.tsIndividuelleVersetzungsbemerkungen)) + ',';
		}
		if (typeof obj.foerderbemerkungen !== "undefined") {
			result += '"foerderbemerkungen" : ' + ((!obj.foerderbemerkungen) ? 'null' : JSON.stringify(obj.foerderbemerkungen)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMLeistungBemerkungen(obj : unknown) : ENMLeistungBemerkungen {
	return obj as ENMLeistungBemerkungen;
}
