import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingVorlageParameter, cast_de_svws_nrw_core_data_reporting_ReportingVorlageParameter } from '../../../core/data/reporting/ReportingVorlageParameter';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingVorlageParameterGruppe extends JavaObject {

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
	public istSichtbar: string = "";

	/**
	 * Die Anzahl der Grid-Spalten, die für die Gruppe zur Verfügung stehen.
	 */
	public anzahlSpalten: number = 1;

	/**
	 * Die Liste der ReportingVorlageParameter, die zu dieser Gruppe gehören.
	 */
	public reportingVorlageParameterList: List<ReportingVorlageParameter> = new ArrayList<ReportingVorlageParameter>();


	/**
	 * Konstruktor für die Klasse.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingVorlageParameterGruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingVorlageParameterGruppe'].includes(name);
	}

	public static readonly class = new Class<ReportingVorlageParameterGruppe>('de.svws_nrw.core.data.reporting.ReportingVorlageParameterGruppe');

	public static transpilerFromJSON(json: string): ReportingVorlageParameterGruppe {
		const obj = JSON.parse(json) as Partial<ReportingVorlageParameterGruppe>;
		const result = new ReportingVorlageParameterGruppe();
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.anzahlSpalten === undefined)
			throw new Error('invalid json format, missing attribute anzahlSpalten');
		result.anzahlSpalten = obj.anzahlSpalten;
		if (obj.reportingVorlageParameterList !== undefined) {
			for (const elem of obj.reportingVorlageParameterList) {
				result.reportingVorlageParameterList.add(ReportingVorlageParameter.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingVorlageParameterGruppe): string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"istSichtbar" : ' + JSON.stringify(obj.istSichtbar) + ',';
		result += '"anzahlSpalten" : ' + obj.anzahlSpalten.toString() + ',';
		result += '"reportingVorlageParameterList" : [ ';
		for (let i = 0; i < obj.reportingVorlageParameterList.size(); i++) {
			const elem = obj.reportingVorlageParameterList.get(i);
			result += ReportingVorlageParameter.transpilerToJSON(elem);
			if (i < obj.reportingVorlageParameterList.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingVorlageParameterGruppe>): string {
		let result = '{';
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + JSON.stringify(obj.istSichtbar) + ',';
		}
		if (obj.anzahlSpalten !== undefined) {
			result += '"anzahlSpalten" : ' + obj.anzahlSpalten.toString() + ',';
		}
		if (obj.reportingVorlageParameterList !== undefined) {
			result += '"reportingVorlageParameterList" : [ ';
			for (let i = 0; i < obj.reportingVorlageParameterList.size(); i++) {
				const elem = obj.reportingVorlageParameterList.get(i);
				result += ReportingVorlageParameter.transpilerToJSON(elem);
				if (i < obj.reportingVorlageParameterList.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingVorlageParameterGruppe(obj: unknown): ReportingVorlageParameterGruppe {
	return obj as ReportingVorlageParameterGruppe;
}
