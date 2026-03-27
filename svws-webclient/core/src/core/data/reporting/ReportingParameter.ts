import { JavaObject } from '../../../java/lang/JavaObject';
import { ReportingFilterDefinitionGruppe } from '../../../core/data/reporting/ReportingFilterDefinitionGruppe';
import { ReportingSortierungDefinitionGruppe } from '../../../core/data/reporting/ReportingSortierungDefinitionGruppe';
import { ReportingEMailDaten } from '../../../core/data/reporting/ReportingEMailDaten';
import { ArrayList } from '../../../java/util/ArrayList';
import { ReportingReportvorlageParameterGruppe } from '../../../core/data/reporting/ReportingReportvorlageParameterGruppe';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ReportingAusgabeformat } from '../../../core/types/reporting/ReportingAusgabeformat';

export class ReportingParameter extends JavaObject {

	/**
	 * Die ID des Schuljahres, auf den sich die Ausgabe des Reports beziehen soll.
	 */
	public idSchuljahresabschnitt: number = -1;

	/**
	 * Die Optionen für das Dateiformat, in dem der Report ausgegeben werden soll, angegeben als Wert gemäß CoreType {@link ReportingAusgabeformat}
	 */
	public ausgabeformatOptionen: List<number> = new ArrayList<number>();

	/**
	 * Das Dateiformat, in dem der Report ausgegeben werden soll, angegeben als Wert gemäß CoreType {@link ReportingAusgabeformat}
	 */
	public ausgabeformat: number = ReportingAusgabeformat.PDF.getId();

	/**
	 * Die Bezeichnung des auszugebenden Reports gemäß Definition im CoreType {@link ReportingReportvorlage}
	 */
	public reportvorlage: string = "";

	/**
	 * Eine ID zum Objekt, das die Hauptdaten-IDs enthält, wie z. B. die ID eines Blockungsergebnisses oder eines Stundenplans. Gibt es kein solches Objekt,
	 *   so ist der Wert kleiner 0.
	 */
	public idHauptdatenObjekt: number = -1;

	/**
	 * Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDFs.
	 */
	public idsHauptdaten: List<number> = new ArrayList<number>();

	/**
	 * Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten.
	 */
	public idsDetaildaten: List<number> = new ArrayList<number>();

	/**
	 * Eine Liste mit Gruppen von freien, typisierten Report-Parameter-Werten, die in Templates direkt über ihren Namen nutzbar sind.
	 */
	public reportvorlageParameterGruppen: List<ReportingReportvorlageParameterGruppe> = new ArrayList<ReportingReportvorlageParameterGruppe>();

	/**
	 * Typenspezifische Sortierdefinitionen, die für die Sortierung von ProxyTyp-Objekten verwendet werden sollen.
	 */
	public sortierungDefinitionenGruppen: List<ReportingSortierungDefinitionGruppe> = new ArrayList<ReportingSortierungDefinitionGruppe>();

	/**
	 * Typenspezifische Filterdefinitionen, die für die Filterung von ProxyTyp-Objekten verwendet werden sollen.
	 */
	public filterDefinitionenGruppen: List<ReportingFilterDefinitionGruppe> = new ArrayList<ReportingFilterDefinitionGruppe>();

	/**
	 * Parameter, der die Daten für den E-Mail-Versand enthält.
	 */
	public eMailDaten: ReportingEMailDaten | null = new ReportingEMailDaten();


	/**
	 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
	 * Sie enthält die Daten und Einstellungen, welche im Rahmen der serverseitigen Report-Generierung genutzt werden sollen.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.reporting.ReportingParameter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.reporting.ReportingParameter'].includes(name);
	}

	public static readonly class = new Class<ReportingParameter>('de.svws_nrw.core.data.reporting.ReportingParameter');

	public static transpilerFromJSON(json: string): ReportingParameter {
		const obj = JSON.parse(json) as Partial<ReportingParameter>;
		const result = new ReportingParameter();
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.ausgabeformatOptionen !== undefined) {
			for (const elem of obj.ausgabeformatOptionen) {
				result.ausgabeformatOptionen.add(elem);
			}
		}
		if (obj.ausgabeformat === undefined)
			throw new Error('invalid json format, missing attribute ausgabeformat');
		result.ausgabeformat = obj.ausgabeformat;
		if (obj.reportvorlage === undefined)
			throw new Error('invalid json format, missing attribute reportvorlage');
		result.reportvorlage = obj.reportvorlage;
		if (obj.idHauptdatenObjekt === undefined)
			throw new Error('invalid json format, missing attribute idHauptdatenObjekt');
		result.idHauptdatenObjekt = obj.idHauptdatenObjekt;
		if (obj.idsHauptdaten !== undefined) {
			for (const elem of obj.idsHauptdaten) {
				result.idsHauptdaten.add(elem);
			}
		}
		if (obj.idsDetaildaten !== undefined) {
			for (const elem of obj.idsDetaildaten) {
				result.idsDetaildaten.add(elem);
			}
		}
		if (obj.reportvorlageParameterGruppen !== undefined) {
			for (const elem of obj.reportvorlageParameterGruppen) {
				result.reportvorlageParameterGruppen.add(ReportingReportvorlageParameterGruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.sortierungDefinitionenGruppen !== undefined) {
			for (const elem of obj.sortierungDefinitionenGruppen) {
				result.sortierungDefinitionenGruppen.add(ReportingSortierungDefinitionGruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.filterDefinitionenGruppen !== undefined) {
			for (const elem of obj.filterDefinitionenGruppen) {
				result.filterDefinitionenGruppen.add(ReportingFilterDefinitionGruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.eMailDaten = ((obj.eMailDaten === undefined) || (obj.eMailDaten === null)) ? null : ReportingEMailDaten.transpilerFromJSON(JSON.stringify(obj.eMailDaten));
		return result;
	}

	public static transpilerToJSON(obj: ReportingParameter): string {
		let result = '{';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"ausgabeformatOptionen" : [ ';
		for (let i = 0; i < obj.ausgabeformatOptionen.size(); i++) {
			const elem = obj.ausgabeformatOptionen.get(i);
			result += elem.toString();
			if (i < obj.ausgabeformatOptionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"ausgabeformat" : ' + obj.ausgabeformat.toString() + ',';
		result += '"reportvorlage" : ' + JSON.stringify(obj.reportvorlage) + ',';
		result += '"idHauptdatenObjekt" : ' + obj.idHauptdatenObjekt.toString() + ',';
		result += '"idsHauptdaten" : [ ';
		for (let i = 0; i < obj.idsHauptdaten.size(); i++) {
			const elem = obj.idsHauptdaten.get(i);
			result += elem.toString();
			if (i < obj.idsHauptdaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idsDetaildaten" : [ ';
		for (let i = 0; i < obj.idsDetaildaten.size(); i++) {
			const elem = obj.idsDetaildaten.get(i);
			result += elem.toString();
			if (i < obj.idsDetaildaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"reportvorlageParameterGruppen" : [ ';
		for (let i = 0; i < obj.reportvorlageParameterGruppen.size(); i++) {
			const elem = obj.reportvorlageParameterGruppen.get(i);
			result += ReportingReportvorlageParameterGruppe.transpilerToJSON(elem);
			if (i < obj.reportvorlageParameterGruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sortierungDefinitionenGruppen" : [ ';
		for (let i = 0; i < obj.sortierungDefinitionenGruppen.size(); i++) {
			const elem = obj.sortierungDefinitionenGruppen.get(i);
			result += ReportingSortierungDefinitionGruppe.transpilerToJSON(elem);
			if (i < obj.sortierungDefinitionenGruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"filterDefinitionenGruppen" : [ ';
		for (let i = 0; i < obj.filterDefinitionenGruppen.size(); i++) {
			const elem = obj.filterDefinitionenGruppen.get(i);
			result += ReportingFilterDefinitionGruppe.transpilerToJSON(elem);
			if (i < obj.filterDefinitionenGruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"eMailDaten" : ' + ((obj.eMailDaten === null) ? 'null' : ReportingEMailDaten.transpilerToJSON(obj.eMailDaten)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReportingParameter>): string {
		let result = '{';
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.ausgabeformatOptionen !== undefined) {
			result += '"ausgabeformatOptionen" : [ ';
			for (let i = 0; i < obj.ausgabeformatOptionen.size(); i++) {
				const elem = obj.ausgabeformatOptionen.get(i);
				result += elem.toString();
				if (i < obj.ausgabeformatOptionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.ausgabeformat !== undefined) {
			result += '"ausgabeformat" : ' + obj.ausgabeformat.toString() + ',';
		}
		if (obj.reportvorlage !== undefined) {
			result += '"reportvorlage" : ' + JSON.stringify(obj.reportvorlage) + ',';
		}
		if (obj.idHauptdatenObjekt !== undefined) {
			result += '"idHauptdatenObjekt" : ' + obj.idHauptdatenObjekt.toString() + ',';
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
		if (obj.reportvorlageParameterGruppen !== undefined) {
			result += '"reportvorlageParameterGruppen" : [ ';
			for (let i = 0; i < obj.reportvorlageParameterGruppen.size(); i++) {
				const elem = obj.reportvorlageParameterGruppen.get(i);
				result += ReportingReportvorlageParameterGruppe.transpilerToJSON(elem);
				if (i < obj.reportvorlageParameterGruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sortierungDefinitionenGruppen !== undefined) {
			result += '"sortierungDefinitionenGruppen" : [ ';
			for (let i = 0; i < obj.sortierungDefinitionenGruppen.size(); i++) {
				const elem = obj.sortierungDefinitionenGruppen.get(i);
				result += ReportingSortierungDefinitionGruppe.transpilerToJSON(elem);
				if (i < obj.sortierungDefinitionenGruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.filterDefinitionenGruppen !== undefined) {
			result += '"filterDefinitionenGruppen" : [ ';
			for (let i = 0; i < obj.filterDefinitionenGruppen.size(); i++) {
				const elem = obj.filterDefinitionenGruppen.get(i);
				result += ReportingFilterDefinitionGruppe.transpilerToJSON(elem);
				if (i < obj.filterDefinitionenGruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.eMailDaten !== undefined) {
			result += '"eMailDaten" : ' + ((obj.eMailDaten === null) ? 'null' : ReportingEMailDaten.transpilerToJSON(obj.eMailDaten)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_reporting_ReportingParameter(obj: unknown): ReportingParameter {
	return obj as ReportingParameter;
}
