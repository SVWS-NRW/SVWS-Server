import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ReportingAusgabeformat } from '../../../core/types/reporting/ReportingAusgabeformat';

export class ReportingParameter extends JavaObject {

	/**
	 * Die ID des Schuljahres, auf den sich die Ausgabe des Reports beziehen soll.
	 */
	public idSchuljahresabschnitt : number = -1;

	/**
	 * Das Dateiformat, in dem der Report ausgegeben werden soll, als Wert gemäß CoreType {@link ReportingAusgabeformat}
	 */
	public ausgabeformat : number = ReportingAusgabeformat.PDF.getId();

	/**
	 * Die Bezeichnung des auszugebenden Reports gemäß Definition im CoreType {@link ReportingReportvorlage}
	 */
	public reportvorlage : string = "";

	/**
	 * Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF.
	 */
	public idsHauptdaten : List<number> = new ArrayList<number>();

	/**
	 * Legt fest, ob pro Datensatz der Hauptdaten eine einzelne PDF-Datei erzeugt werden soll.
	 */
	public einzelausgabeHauptdaten : boolean = false;

	/**
	 * Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten.
	 */
	public idsDetaildaten : List<number> = new ArrayList<number>();

	/**
	 * Legt fest, ob pro Datensatz der Detaildaten eine einzelne PDF-Datei erzeugt werden soll.
	 */
	public einzelausgabeDetaildaten : boolean = false;

	/**
	 * Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.
	 */
	public detailLevel : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingParameter';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingParameter'].includes(name);
	}

	public static class = new Class<ReportingParameter>('de.svws_nrw.core.data.reporting.ReportingParameter');

	public static transpilerFromJSON(json : string): ReportingParameter {
		const obj = JSON.parse(json) as Partial<ReportingParameter>;
		const result = new ReportingParameter();
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.ausgabeformat === undefined)
			throw new Error('invalid json format, missing attribute ausgabeformat');
		result.ausgabeformat = obj.ausgabeformat;
		if (obj.reportvorlage === undefined)
			throw new Error('invalid json format, missing attribute reportvorlage');
		result.reportvorlage = obj.reportvorlage;
		if (obj.idsHauptdaten !== undefined) {
			for (const elem of obj.idsHauptdaten) {
				result.idsHauptdaten.add(elem);
			}
		}
		if (obj.einzelausgabeHauptdaten === undefined)
			throw new Error('invalid json format, missing attribute einzelausgabeHauptdaten');
		result.einzelausgabeHauptdaten = obj.einzelausgabeHauptdaten;
		if (obj.idsDetaildaten !== undefined) {
			for (const elem of obj.idsDetaildaten) {
				result.idsDetaildaten.add(elem);
			}
		}
		if (obj.einzelausgabeDetaildaten === undefined)
			throw new Error('invalid json format, missing attribute einzelausgabeDetaildaten');
		result.einzelausgabeDetaildaten = obj.einzelausgabeDetaildaten;
		if (obj.detailLevel === undefined)
			throw new Error('invalid json format, missing attribute detailLevel');
		result.detailLevel = obj.detailLevel;
		return result;
	}

	public static transpilerToJSON(obj : ReportingParameter) : string {
		let result = '{';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"ausgabeformat" : ' + obj.ausgabeformat.toString() + ',';
		result += '"reportvorlage" : ' + JSON.stringify(obj.reportvorlage) + ',';
		result += '"idsHauptdaten" : [ ';
		for (let i = 0; i < obj.idsHauptdaten.size(); i++) {
			const elem = obj.idsHauptdaten.get(i);
			result += elem.toString();
			if (i < obj.idsHauptdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"einzelausgabeHauptdaten" : ' + obj.einzelausgabeHauptdaten.toString() + ',';
		result += '"idsDetaildaten" : [ ';
		for (let i = 0; i < obj.idsDetaildaten.size(); i++) {
			const elem = obj.idsDetaildaten.get(i);
			result += elem.toString();
			if (i < obj.idsDetaildaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"einzelausgabeDetaildaten" : ' + obj.einzelausgabeDetaildaten.toString() + ',';
		result += '"detailLevel" : ' + obj.detailLevel.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ReportingParameter>) : string {
		let result = '{';
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.ausgabeformat !== undefined) {
			result += '"ausgabeformat" : ' + obj.ausgabeformat.toString() + ',';
		}
		if (obj.reportvorlage !== undefined) {
			result += '"reportvorlage" : ' + JSON.stringify(obj.reportvorlage) + ',';
		}
		if (obj.idsHauptdaten !== undefined) {
			result += '"idsHauptdaten" : [ ';
			for (let i = 0; i < obj.idsHauptdaten.size(); i++) {
				const elem = obj.idsHauptdaten.get(i);
				result += elem.toString();
				if (i < obj.idsHauptdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.einzelausgabeHauptdaten !== undefined) {
			result += '"einzelausgabeHauptdaten" : ' + obj.einzelausgabeHauptdaten.toString() + ',';
		}
		if (obj.idsDetaildaten !== undefined) {
			result += '"idsDetaildaten" : [ ';
			for (let i = 0; i < obj.idsDetaildaten.size(); i++) {
				const elem = obj.idsDetaildaten.get(i);
				result += elem.toString();
				if (i < obj.idsDetaildaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.einzelausgabeDetaildaten !== undefined) {
			result += '"einzelausgabeDetaildaten" : ' + obj.einzelausgabeDetaildaten.toString() + ',';
		}
		if (obj.detailLevel !== undefined) {
			result += '"detailLevel" : ' + obj.detailLevel.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingParameter(obj : unknown) : ReportingParameter {
	return obj as ReportingParameter;
}
