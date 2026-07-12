import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingEinstellungenBenutzerVorlageGruppe extends JavaObject {

	/**
	 * Die Bezeichnung der Sortier- oder Filtergruppe.
	 */
	public gruppe: string = "";

	/**
	 * Die Bezeichnungen der ausgewählten Definitionen aus den Katalog-Optionen der Gruppe, in Auswahlreihenfolge.
	 */
	public bezeichnungen: List<string> = new ArrayList<string>();


	/**
	 * Diese Klasse enthält die gespeicherte Auswahl einer einzelnen Sortier- oder Filtergruppe.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlageGruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlageGruppe'].includes(name);
	}

	public static readonly class = new Class<ReportingEinstellungenBenutzerVorlageGruppe>('de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlageGruppe');

	public static transpilerFromJSON(json: string): ReportingEinstellungenBenutzerVorlageGruppe {
		const obj = JSON.parse(json) as Partial<ReportingEinstellungenBenutzerVorlageGruppe>;
		const result = new ReportingEinstellungenBenutzerVorlageGruppe();
		if (obj.gruppe === undefined)
			throw new Error('invalid json format, missing attribute gruppe');
		result.gruppe = obj.gruppe;
		if (obj.bezeichnungen !== undefined) {
			for (const elem of obj.bezeichnungen) {
				result.bezeichnungen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingEinstellungenBenutzerVorlageGruppe): string {
		let result = '{';
		result += '"gruppe" : ' + JSON.stringify(obj.gruppe) + ',';
		result += '"bezeichnungen" : [ ';
		for (let i = 0; i < obj.bezeichnungen.size(); i++) {
			const elem = obj.bezeichnungen.get(i);
			result += '"' + elem + '"';
			if (i < obj.bezeichnungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingEinstellungenBenutzerVorlageGruppe>): string {
		let result = '{';
		if (obj.gruppe !== undefined) {
			result += '"gruppe" : ' + JSON.stringify(obj.gruppe) + ',';
		}
		if (obj.bezeichnungen !== undefined) {
			result += '"bezeichnungen" : [ ';
			for (let i = 0; i < obj.bezeichnungen.size(); i++) {
				const elem = obj.bezeichnungen.get(i);
				result += '"' + elem + '"';
				if (i < obj.bezeichnungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingEinstellungenBenutzerVorlageGruppe(obj: unknown): ReportingEinstellungenBenutzerVorlageGruppe {
	return obj as ReportingEinstellungenBenutzerVorlageGruppe;
}
