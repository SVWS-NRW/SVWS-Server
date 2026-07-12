import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ReportingEinstellungenBenutzerVorlagenParameterWert extends JavaObject {

	/**
	 * Der Name des Parameters gemäß dem Katalog der benutzerweiten Parameter.
	 */
	public name: string = "";

	/**
	 * Der gespeicherte Wert des Parameters als Zeichenkette.
	 */
	public wert: string = "";


	/**
	 * Diese Klasse enthält den gespeicherten Wert eines benutzerweiten Report-Parameters.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagenParameterWert';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagenParameterWert'].includes(name);
	}

	public static readonly class = new Class<ReportingEinstellungenBenutzerVorlagenParameterWert>('de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagenParameterWert');

	public static transpilerFromJSON(json: string): ReportingEinstellungenBenutzerVorlagenParameterWert {
		const obj = JSON.parse(json) as Partial<ReportingEinstellungenBenutzerVorlagenParameterWert>;
		const result = new ReportingEinstellungenBenutzerVorlagenParameterWert();
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.wert === undefined)
			throw new Error('invalid json format, missing attribute wert');
		result.wert = obj.wert;
		return result;
	}

	public static transpilerToJSON(obj: ReportingEinstellungenBenutzerVorlagenParameterWert): string {
		let result = '{';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingEinstellungenBenutzerVorlagenParameterWert>): string {
		let result = '{';
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.wert !== undefined) {
			result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingEinstellungenBenutzerVorlagenParameterWert(obj: unknown): ReportingEinstellungenBenutzerVorlagenParameterWert {
	return obj as ReportingEinstellungenBenutzerVorlagenParameterWert;
}
