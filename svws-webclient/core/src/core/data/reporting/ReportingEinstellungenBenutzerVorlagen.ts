import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingEinstellungenBenutzerVorlagenParameterWert } from '../../../core/data/reporting/ReportingEinstellungenBenutzerVorlagenParameterWert';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingEinstellungenBenutzerVorlagen extends JavaObject {

	/**
	 * Die Version des Speicherformats, um künftige Format-Migrationen zu ermöglichen.
	 */
	public version: number = 1;

	/**
	 * Die gespeicherten Werte der benutzerweiten Parameter. Fehlt ein Katalog-Parameter, so gilt sein Katalog-Default.
	 */
	public parameterWerte: List<ReportingEinstellungenBenutzerVorlagenParameterWert> = new ArrayList<ReportingEinstellungenBenutzerVorlagenParameterWert>();


	/**
	 * Diese Klasse definiert das Speicherformat der benutzerweiten Reporting-Einstellungen eines Benutzers in der
	 * Client-Konfiguration.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagen'].includes(name);
	}

	public static readonly class = new Class<ReportingEinstellungenBenutzerVorlagen>('de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagen');

	public static transpilerFromJSON(json: string): ReportingEinstellungenBenutzerVorlagen {
		const obj = JSON.parse(json) as Partial<ReportingEinstellungenBenutzerVorlagen>;
		const result = new ReportingEinstellungenBenutzerVorlagen();
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.parameterWerte !== undefined) {
			for (const elem of obj.parameterWerte) {
				result.parameterWerte.add(ReportingEinstellungenBenutzerVorlagenParameterWert.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingEinstellungenBenutzerVorlagen): string {
		let result = '{';
		result += '"version" : ' + obj.version.toString() + ',';
		result += '"parameterWerte" : [ ';
		for (let i = 0; i < obj.parameterWerte.size(); i++) {
			const elem = obj.parameterWerte.get(i);
			result += ReportingEinstellungenBenutzerVorlagenParameterWert.transpilerToJSON(elem);
			if (i < obj.parameterWerte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingEinstellungenBenutzerVorlagen>): string {
		let result = '{';
		if (obj.version !== undefined) {
			result += '"version" : ' + obj.version.toString() + ',';
		}
		if (obj.parameterWerte !== undefined) {
			result += '"parameterWerte" : [ ';
			for (let i = 0; i < obj.parameterWerte.size(); i++) {
				const elem = obj.parameterWerte.get(i);
				result += ReportingEinstellungenBenutzerVorlagenParameterWert.transpilerToJSON(elem);
				if (i < obj.parameterWerte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingEinstellungenBenutzerVorlagen(obj: unknown): ReportingEinstellungenBenutzerVorlagen {
	return obj as ReportingEinstellungenBenutzerVorlagen;
}
