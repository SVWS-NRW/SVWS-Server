import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingEinstellungenBenutzerVorlageGruppe } from '../../../core/data/reporting/ReportingEinstellungenBenutzerVorlageGruppe';
import { ReportingEinstellungenBenutzerVorlagenParameterWert } from '../../../core/data/reporting/ReportingEinstellungenBenutzerVorlagenParameterWert';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ReportingEinstellungenBenutzerVorlage extends JavaObject {

	/**
	 * Die Version des Speicherformats, um künftige Format-Migrationen zu ermöglichen.
	 */
	public version: number = 1;

	/**
	 * Die gespeicherten Werte der vorlagenspezifischen Parameter. Fehlt ein Parameter, so gilt sein Katalog-Default.
	 */
	public parameterWerte: List<ReportingEinstellungenBenutzerVorlagenParameterWert> = new ArrayList<ReportingEinstellungenBenutzerVorlagenParameterWert>();

	/**
	 *  Die gespeicherten Sortierungsauswahlen je Sortiergruppe. Verwendet dieselbe Struktur wie {@link #filterungsauswahlen},
	 *  da beide Fälle identisch sind: eine Gruppe (identifiziert über ihre {@code bezeichnung}) und die darin ausgewählten
	 *  Katalog-Einträge als geordnete Liste ihrer {@code bezeichnung}en.
	 */
	public sortierungsauswahlen: List<ReportingEinstellungenBenutzerVorlageGruppe> = new ArrayList<ReportingEinstellungenBenutzerVorlageGruppe>();

	/**
	 * Die gespeicherten Filterauswahlen je Filtergruppe.
	 */
	public filterungsauswahlen: List<ReportingEinstellungenBenutzerVorlageGruppe> = new ArrayList<ReportingEinstellungenBenutzerVorlageGruppe>();


	/**
	 * Diese Klasse definiert das Speicherformat der benutzerspezifischen Einstellungen einer einzelnen Reportvorlage.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlage';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlage'].includes(name);
	}

	public static readonly class = new Class<ReportingEinstellungenBenutzerVorlage>('de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlage');

	public static transpilerFromJSON(json: string): ReportingEinstellungenBenutzerVorlage {
		const obj = JSON.parse(json) as Partial<ReportingEinstellungenBenutzerVorlage>;
		const result = new ReportingEinstellungenBenutzerVorlage();
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.parameterWerte !== undefined) {
			for (const elem of obj.parameterWerte) {
				result.parameterWerte.add(ReportingEinstellungenBenutzerVorlagenParameterWert.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.sortierungsauswahlen !== undefined) {
			for (const elem of obj.sortierungsauswahlen) {
				result.sortierungsauswahlen.add(ReportingEinstellungenBenutzerVorlageGruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.filterungsauswahlen !== undefined) {
			for (const elem of obj.filterungsauswahlen) {
				result.filterungsauswahlen.add(ReportingEinstellungenBenutzerVorlageGruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ReportingEinstellungenBenutzerVorlage): string {
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
		result += '"sortierungsauswahlen" : [ ';
		for (let i = 0; i < obj.sortierungsauswahlen.size(); i++) {
			const elem = obj.sortierungsauswahlen.get(i);
			result += ReportingEinstellungenBenutzerVorlageGruppe.transpilerToJSON(elem);
			if (i < obj.sortierungsauswahlen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"filterungsauswahlen" : [ ';
		for (let i = 0; i < obj.filterungsauswahlen.size(); i++) {
			const elem = obj.filterungsauswahlen.get(i);
			result += ReportingEinstellungenBenutzerVorlageGruppe.transpilerToJSON(elem);
			if (i < obj.filterungsauswahlen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingEinstellungenBenutzerVorlage>): string {
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
		if (obj.sortierungsauswahlen !== undefined) {
			result += '"sortierungsauswahlen" : [ ';
			for (let i = 0; i < obj.sortierungsauswahlen.size(); i++) {
				const elem = obj.sortierungsauswahlen.get(i);
				result += ReportingEinstellungenBenutzerVorlageGruppe.transpilerToJSON(elem);
				if (i < obj.sortierungsauswahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.filterungsauswahlen !== undefined) {
			result += '"filterungsauswahlen" : [ ';
			for (let i = 0; i < obj.filterungsauswahlen.size(); i++) {
				const elem = obj.filterungsauswahlen.get(i);
				result += ReportingEinstellungenBenutzerVorlageGruppe.transpilerToJSON(elem);
				if (i < obj.filterungsauswahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingEinstellungenBenutzerVorlage(obj: unknown): ReportingEinstellungenBenutzerVorlage {
	return obj as ReportingEinstellungenBenutzerVorlage;
}
