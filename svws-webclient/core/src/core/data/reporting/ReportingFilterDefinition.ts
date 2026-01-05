import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ReportingFilterKriterium } from '../../../core/data/reporting/ReportingFilterKriterium';

export class ReportingFilterDefinition extends JavaObject {

	/**
	 * Der Typname des zu filternden Reporting-Datentyps, z. B. 'ReportingFach'.
	 */
	public typ: string = "";

	/**
	 * Liste der Filterkriterien.
	 */
	public kriterien: List<ReportingFilterKriterium> = new ArrayList<ReportingFilterKriterium>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingFilterDefinition';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingFilterDefinition'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterDefinition>('de.svws_nrw.core.data.reporting.ReportingFilterDefinition');

	public static transpilerFromJSON(json: string): ReportingFilterDefinition {
		const obj = JSON.parse(json) as Partial<ReportingFilterDefinition>;
		const result = new ReportingFilterDefinition();
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.kriterien !== undefined) {
			for (const elem of obj.kriterien) {
				result.kriterien.add(ReportingFilterKriterium.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingFilterDefinition): string {
		let result = '{';
		result += '"typ" : ' + JSON.stringify(obj.typ) + ',';
		result += '"kriterien" : [ ';
		for (let i = 0; i < obj.kriterien.size(); i++) {
			const elem = obj.kriterien.get(i);
			result += ReportingFilterKriterium.transpilerToJSON(elem);
			if (i < obj.kriterien.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingFilterDefinition>): string {
		let result = '{';
		if (obj.typ !== undefined) {
			result += '"typ" : ' + JSON.stringify(obj.typ) + ',';
		}
		if (obj.kriterien !== undefined) {
			result += '"kriterien" : [ ';
			for (let i = 0; i < obj.kriterien.size(); i++) {
				const elem = obj.kriterien.get(i);
				result += ReportingFilterKriterium.transpilerToJSON(elem);
				if (i < obj.kriterien.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingFilterDefinition(obj: unknown): ReportingFilterDefinition {
	return obj as ReportingFilterDefinition;
}
