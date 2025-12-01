import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingVorlageParameterTyp } from '../../../core/types/reporting/ReportingVorlageParameterTyp';
import { Class } from '../../../java/lang/Class';

export class ReportingVorlageParameter extends JavaObject {

	/**
	 * Der Name des Vorlage-Parameters, wie er später im HTML-Template verwendet wird.
	 */
	public name: string = "";

	/**
	 * Der Typ des Wertes des Vorlage-Parameters.
	 */
	public typ: number = ReportingVorlageParameterTyp.UNDEFINED.getId();

	/**
	 * Der Wert des Vorlage-Parameters.
	 */
	public wert: string = "";


	/**
	 * Konstruktor für die Klasse.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingVorlageParameter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingVorlageParameter'].includes(name);
	}

	public static readonly class = new Class<ReportingVorlageParameter>('de.svws_nrw.core.data.reporting.ReportingVorlageParameter');

	public static transpilerFromJSON(json: string): ReportingVorlageParameter {
		const obj = JSON.parse(json) as Partial<ReportingVorlageParameter>;
		const result = new ReportingVorlageParameter();
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.wert === undefined)
			throw new Error('invalid json format, missing attribute wert');
		result.wert = obj.wert;
		return result;
	}

	public static transpilerToJSON(obj: ReportingVorlageParameter): string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"typ" : ' + obj.typ.toString() + ',';
		result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingVorlageParameter>): string {
		let result = '{';
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + obj.typ.toString() + ',';
		}
		if (obj.wert !== undefined) {
			result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingVorlageParameter(obj: unknown): ReportingVorlageParameter {
	return obj as ReportingVorlageParameter;
}
