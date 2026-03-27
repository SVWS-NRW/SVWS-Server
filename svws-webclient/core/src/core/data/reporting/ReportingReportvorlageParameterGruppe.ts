import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingReportvorlageParameter, cast_de_svws_nrw_core_data_reporting_ReportingReportvorlageParameter } from '../../../core/data/reporting/ReportingReportvorlageParameter';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingReportvorlageParameterGruppe extends JavaObject {

	/**
	 * Der Name der Gruppe.
	 */
	public name: string = "";

	/**
	 * Die Beschreibung der Gruppe.
	 */
	public beschreibung: string = "";

	/**
	 * Gibt an, ob die Gruppe in der UI sichtbar sein soll.
	 */
	public uiIstSichtbar: boolean = true;

	/**
	 * Die Anzahl der Grid-Spalten, die für die Gruppe zur Verfügung stehen.
	 */
	public uiAnzahlSpalten: number = 1;

	/**
	 * Die Liste der ReportingVorlageParameter, die zu dieser Gruppe gehören.
	 */
	public reportvorlageParameter: List<ReportingReportvorlageParameter> = new ArrayList<ReportingReportvorlageParameter>();


	/**
	 * Konstruktor für die Klasse.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageParameterGruppe>('de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe');

	public static transpilerFromJSON(json: string): ReportingReportvorlageParameterGruppe {
		const obj = JSON.parse(json) as Partial<ReportingReportvorlageParameterGruppe>;
		const result = new ReportingReportvorlageParameterGruppe();
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.uiIstSichtbar === undefined)
			throw new Error('invalid json format, missing attribute uiIstSichtbar');
		result.uiIstSichtbar = obj.uiIstSichtbar;
		if (obj.uiAnzahlSpalten === undefined)
			throw new Error('invalid json format, missing attribute uiAnzahlSpalten');
		result.uiAnzahlSpalten = obj.uiAnzahlSpalten;
		if (obj.reportvorlageParameter !== undefined) {
			for (const elem of obj.reportvorlageParameter) {
				result.reportvorlageParameter.add(ReportingReportvorlageParameter.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingReportvorlageParameterGruppe): string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"uiIstSichtbar" : ' + obj.uiIstSichtbar.toString() + ',';
		result += '"uiAnzahlSpalten" : ' + obj.uiAnzahlSpalten.toString() + ',';
		result += '"reportvorlageParameter" : [ ';
		for (let i = 0; i < obj.reportvorlageParameter.size(); i++) {
			const elem = obj.reportvorlageParameter.get(i);
			result += ReportingReportvorlageParameter.transpilerToJSON(elem);
			if (i < obj.reportvorlageParameter.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingReportvorlageParameterGruppe>): string {
		let result = '{';
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.uiIstSichtbar !== undefined) {
			result += '"uiIstSichtbar" : ' + obj.uiIstSichtbar.toString() + ',';
		}
		if (obj.uiAnzahlSpalten !== undefined) {
			result += '"uiAnzahlSpalten" : ' + obj.uiAnzahlSpalten.toString() + ',';
		}
		if (obj.reportvorlageParameter !== undefined) {
			result += '"reportvorlageParameter" : [ ';
			for (let i = 0; i < obj.reportvorlageParameter.size(); i++) {
				const elem = obj.reportvorlageParameter.get(i);
				result += ReportingReportvorlageParameter.transpilerToJSON(elem);
				if (i < obj.reportvorlageParameter.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingReportvorlageParameterGruppe(obj: unknown): ReportingReportvorlageParameterGruppe {
	return obj as ReportingReportvorlageParameterGruppe;
}
