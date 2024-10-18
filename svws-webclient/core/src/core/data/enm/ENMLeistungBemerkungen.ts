import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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
	 * Der Zeitstempel mit den letzten Änderungen zur Lern und Leistungsentwicklung (LELS) in den Fächern
	 */
	public tsLELS : string | null = null;

	/**
	 * Schulform-Empfehlungen
	 */
	public schulformEmpf : string | null = null;

	/**
	 * Der Zeitstempel mit den letzten Änderungen zu den Schulform-Empfehlungen
	 */
	public tsSchulformEmpf : string | null = null;

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

	/**
	 * Der Zeitstempel mit den letzten Änderungen zu den Förderbemerkungen
	 */
	public tsFoerderbemerkungen : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMLeistungBemerkungen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMLeistungBemerkungen'].includes(name);
	}

	public static class = new Class<ENMLeistungBemerkungen>('de.svws_nrw.core.data.enm.ENMLeistungBemerkungen');

	public static transpilerFromJSON(json : string): ENMLeistungBemerkungen {
		const obj = JSON.parse(json) as Partial<ENMLeistungBemerkungen>;
		const result = new ENMLeistungBemerkungen();
		result.ASV = (obj.ASV === undefined) ? null : obj.ASV === null ? null : obj.ASV;
		result.tsASV = (obj.tsASV === undefined) ? null : obj.tsASV === null ? null : obj.tsASV;
		result.AUE = (obj.AUE === undefined) ? null : obj.AUE === null ? null : obj.AUE;
		result.tsAUE = (obj.tsAUE === undefined) ? null : obj.tsAUE === null ? null : obj.tsAUE;
		result.ZB = (obj.ZB === undefined) ? null : obj.ZB === null ? null : obj.ZB;
		result.tsZB = (obj.tsZB === undefined) ? null : obj.tsZB === null ? null : obj.tsZB;
		result.LELS = (obj.LELS === undefined) ? null : obj.LELS === null ? null : obj.LELS;
		result.tsLELS = (obj.tsLELS === undefined) ? null : obj.tsLELS === null ? null : obj.tsLELS;
		result.schulformEmpf = (obj.schulformEmpf === undefined) ? null : obj.schulformEmpf === null ? null : obj.schulformEmpf;
		result.tsSchulformEmpf = (obj.tsSchulformEmpf === undefined) ? null : obj.tsSchulformEmpf === null ? null : obj.tsSchulformEmpf;
		result.individuelleVersetzungsbemerkungen = (obj.individuelleVersetzungsbemerkungen === undefined) ? null : obj.individuelleVersetzungsbemerkungen === null ? null : obj.individuelleVersetzungsbemerkungen;
		result.tsIndividuelleVersetzungsbemerkungen = (obj.tsIndividuelleVersetzungsbemerkungen === undefined) ? null : obj.tsIndividuelleVersetzungsbemerkungen === null ? null : obj.tsIndividuelleVersetzungsbemerkungen;
		result.foerderbemerkungen = (obj.foerderbemerkungen === undefined) ? null : obj.foerderbemerkungen === null ? null : obj.foerderbemerkungen;
		result.tsFoerderbemerkungen = (obj.tsFoerderbemerkungen === undefined) ? null : obj.tsFoerderbemerkungen === null ? null : obj.tsFoerderbemerkungen;
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
		result += '"tsLELS" : ' + ((!obj.tsLELS) ? 'null' : JSON.stringify(obj.tsLELS)) + ',';
		result += '"schulformEmpf" : ' + ((!obj.schulformEmpf) ? 'null' : JSON.stringify(obj.schulformEmpf)) + ',';
		result += '"tsSchulformEmpf" : ' + ((!obj.tsSchulformEmpf) ? 'null' : JSON.stringify(obj.tsSchulformEmpf)) + ',';
		result += '"individuelleVersetzungsbemerkungen" : ' + ((!obj.individuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.individuelleVersetzungsbemerkungen)) + ',';
		result += '"tsIndividuelleVersetzungsbemerkungen" : ' + ((!obj.tsIndividuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.tsIndividuelleVersetzungsbemerkungen)) + ',';
		result += '"foerderbemerkungen" : ' + ((!obj.foerderbemerkungen) ? 'null' : JSON.stringify(obj.foerderbemerkungen)) + ',';
		result += '"tsFoerderbemerkungen" : ' + ((!obj.tsFoerderbemerkungen) ? 'null' : JSON.stringify(obj.tsFoerderbemerkungen)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLeistungBemerkungen>) : string {
		let result = '{';
		if (obj.ASV !== undefined) {
			result += '"ASV" : ' + ((!obj.ASV) ? 'null' : JSON.stringify(obj.ASV)) + ',';
		}
		if (obj.tsASV !== undefined) {
			result += '"tsASV" : ' + ((!obj.tsASV) ? 'null' : JSON.stringify(obj.tsASV)) + ',';
		}
		if (obj.AUE !== undefined) {
			result += '"AUE" : ' + ((!obj.AUE) ? 'null' : JSON.stringify(obj.AUE)) + ',';
		}
		if (obj.tsAUE !== undefined) {
			result += '"tsAUE" : ' + ((!obj.tsAUE) ? 'null' : JSON.stringify(obj.tsAUE)) + ',';
		}
		if (obj.ZB !== undefined) {
			result += '"ZB" : ' + ((!obj.ZB) ? 'null' : JSON.stringify(obj.ZB)) + ',';
		}
		if (obj.tsZB !== undefined) {
			result += '"tsZB" : ' + ((!obj.tsZB) ? 'null' : JSON.stringify(obj.tsZB)) + ',';
		}
		if (obj.LELS !== undefined) {
			result += '"LELS" : ' + ((!obj.LELS) ? 'null' : JSON.stringify(obj.LELS)) + ',';
		}
		if (obj.tsLELS !== undefined) {
			result += '"tsLELS" : ' + ((!obj.tsLELS) ? 'null' : JSON.stringify(obj.tsLELS)) + ',';
		}
		if (obj.schulformEmpf !== undefined) {
			result += '"schulformEmpf" : ' + ((!obj.schulformEmpf) ? 'null' : JSON.stringify(obj.schulformEmpf)) + ',';
		}
		if (obj.tsSchulformEmpf !== undefined) {
			result += '"tsSchulformEmpf" : ' + ((!obj.tsSchulformEmpf) ? 'null' : JSON.stringify(obj.tsSchulformEmpf)) + ',';
		}
		if (obj.individuelleVersetzungsbemerkungen !== undefined) {
			result += '"individuelleVersetzungsbemerkungen" : ' + ((!obj.individuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.individuelleVersetzungsbemerkungen)) + ',';
		}
		if (obj.tsIndividuelleVersetzungsbemerkungen !== undefined) {
			result += '"tsIndividuelleVersetzungsbemerkungen" : ' + ((!obj.tsIndividuelleVersetzungsbemerkungen) ? 'null' : JSON.stringify(obj.tsIndividuelleVersetzungsbemerkungen)) + ',';
		}
		if (obj.foerderbemerkungen !== undefined) {
			result += '"foerderbemerkungen" : ' + ((!obj.foerderbemerkungen) ? 'null' : JSON.stringify(obj.foerderbemerkungen)) + ',';
		}
		if (obj.tsFoerderbemerkungen !== undefined) {
			result += '"tsFoerderbemerkungen" : ' + ((!obj.tsFoerderbemerkungen) ? 'null' : JSON.stringify(obj.tsFoerderbemerkungen)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMLeistungBemerkungen(obj : unknown) : ENMLeistungBemerkungen {
	return obj as ENMLeistungBemerkungen;
}
