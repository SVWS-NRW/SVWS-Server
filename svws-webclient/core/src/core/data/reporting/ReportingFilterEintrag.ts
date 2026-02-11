import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingFilterOperation } from '../../../core/types/reporting/ReportingFilterOperation';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingFilterEintrag extends JavaObject {

	/**
	 * Der Name des Attributs, auf das gefiltert werden soll (z. B. 'kuerzel').
	 */
	public attribut: string = "";

	/**
	 * Die Operation, die angewendet werden soll.
	 */
	public operation: number = ReportingFilterOperation.EQUAL.getId();

	/**
	 * Die Werte für den Filter (als Liste, um auch Bedingungen für Operationen wie 'IN' zu ermöglichen).
	 */
	public werte: List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingFilterEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingFilterEintrag'].includes(name);
	}

	public static readonly class = new Class<ReportingFilterEintrag>('de.svws_nrw.core.data.reporting.ReportingFilterEintrag');

	public static transpilerFromJSON(json: string): ReportingFilterEintrag {
		const obj = JSON.parse(json) as Partial<ReportingFilterEintrag>;
		const result = new ReportingFilterEintrag();
		if (obj.attribut === undefined)
			throw new Error('invalid json format, missing attribute attribut');
		result.attribut = obj.attribut;
		if (obj.operation === undefined)
			throw new Error('invalid json format, missing attribute operation');
		result.operation = obj.operation;
		if (obj.werte !== undefined) {
			for (const elem of obj.werte) {
				result.werte.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingFilterEintrag): string {
		let result = '{';
		result += '"attribut" : ' + JSON.stringify(obj.attribut) + ',';
		result += '"operation" : ' + obj.operation.toString() + ',';
		result += '"werte" : [ ';
		for (let i = 0; i < obj.werte.size(); i++) {
			const elem = obj.werte.get(i);
			result += '"' + elem + '"';
			if (i < obj.werte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingFilterEintrag>): string {
		let result = '{';
		if (obj.attribut !== undefined) {
			result += '"attribut" : ' + JSON.stringify(obj.attribut) + ',';
		}
		if (obj.operation !== undefined) {
			result += '"operation" : ' + obj.operation.toString() + ',';
		}
		if (obj.werte !== undefined) {
			result += '"werte" : [ ';
			for (let i = 0; i < obj.werte.size(); i++) {
				const elem = obj.werte.get(i);
				result += '"' + elem + '"';
				if (i < obj.werte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingFilterEintrag(obj: unknown): ReportingFilterEintrag {
	return obj as ReportingFilterEintrag;
}
