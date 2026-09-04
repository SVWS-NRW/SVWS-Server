import type { ReportingParameter } from "@core/core/data/reporting/ReportingParameter";
import type { ReportingReportvorlageParameter } from "@core/core/data/reporting/ReportingReportvorlageParameter";
import type { ReportingReportvorlageParameterGruppe } from "@core/core/data/reporting/ReportingReportvorlageParameterGruppe";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";
import type { InjectionKey } from "vue";

/** Ein Element (Parameter oder Gruppe) mit Anforderungen an ServerMode und Benutzerkompetenzen. */
export type ElementMitAnforderung = { uiIstSichtbar: boolean; uiErforderlicherServerMode: string; uiErforderlicheKompetenzen: List<number> };

/**
 * Die Schnittstelle für den Zustand des Reportings
 */
export interface ReportingState {

	/**
	 * Erzeugt einen PDF-Report aus den übergebenen Parametern
	 *
	 * @param reportingParameter   die ReportingParameter
	 */
	createPDFReport(reportingParameter: ReportingParameter): Promise<void>;

	/**
	 * Erzeugt einen PDF-Report aus den übergebenen Parametern und verschickt diesen per EMail
	 *
	 * @param reportingParameter   die ReportingParameter
	 *
	 * @returns eine SimpleOperationResponse
	 */

	createEMailReport(reportingParameter: ReportingParameter): Promise<SimpleOperationResponse>;

	/**
	 * Erzeugt einen HTML-Report aus den übergebenen Parametern
	 *
	 * @param reportingParameter   die ReportingParameter
	 *
	 * @returns den HTML-Code als String
	 */
	createHTMLReport(reportingParameter: ReportingParameter): Promise<string>;

	/**
	 * Erzeugt einen JSON-String aus den übergebenen Parametern und generiert eine Datei zum Herunterladen
	 *
	 * @param reportingParameter   die ReportingParameter
	 */
	createJSONReportingParameter(reportingParameter: ReportingParameter): Promise<void>;

	/**
	 * Erzeugt ein Log zum Status des Email-Versandts
	 *
	 * @param jobId   die ID des EMail-Auftrags
	 *
	 * @returns eine SimpleOperationResponse mit den Angaben zum EMail-Job
	 */
	fetchEMailJobStatus(jobId: number): Promise<SimpleOperationResponse>;

	/**
	 * Erzeugt ein Log zum abgeschlossenen Email-Versandt
	 *
	 * @param jobId   die ID des EMail-Auftrags
	 *
	 * @returns eine SimpleOperationResponse mit den Angaben zum EMail-Job
	 */

	fetchEMailJobLog(jobId: number): Promise<SimpleOperationResponse>;
	/**
	 * Ein Element ist sichtbar, wenn es als sichtbar markiert ist und der ServerMode passt (sonst wird es ausgeblendet).
	 *
	 * @param element   das Element, das geprüft werden soll
	 *
	 * @returns true, wenn sichtbar
	 */

	istSichtbar(element: ElementMitAnforderung): boolean;
	/**
	 * Ein Element ist aktiv (bedienbar), wenn der Benutzer die erforderlichen Kompetenzen besitzt (sonst wird es deaktiviert dargestellt).
	 *
	 * @param element   das Element, das geprüft werden soll
	 *
	 * @returns true, wenn aktiv
	 */
	istAktiv(element: ElementMitAnforderung): boolean;

	/**
	 * Ein Parameter ist sichtbar, wenn seine Gruppe und er selbst sichtbar sind.
	 *
	 * @param gruppe   die reportvorlageParameterGruppen
	 * @param vp       der Vorlageparameter
	 *
	 * @returns true, wenn der Parameter sichtbar sein soll
	 */
	istParameterSichtbar(gruppe: ReportingReportvorlageParameterGruppe, vp: ReportingReportvorlageParameter): boolean;

	/**
	 * Ein Parameter ist aktiv, wenn seine Gruppe und er selbst die Kompetenzanforderungen erfüllen.
	 *
	 * @param gruppe   die reportvorlageParameterGruppen
	 * @param vp       der Vorlageparameter
	 *
	 * @returns true, wenn der Parameter aktiv ist
	 */
	istParameterAktiv(gruppe: ReportingReportvorlageParameterGruppe, vp: ReportingReportvorlageParameter): boolean;

	/**
	 * Liefert die Bezeichnungen der Kompetenzen, die dem Benutzer für diese Anforderung fehlen (leer, wenn die Anforderung erfüllt ist).
	 *
	 * @param erforderlicheKompetenzen   eine Liste mit den IDs der benötigten Kompetenzen
	 *
	 * @returns ein String mit den erforderlichen Kompetenzen
	 */
	fehlendeKompetenzNamen(erforderlicheKompetenzen: List<number>): string[];

	/**
	 * Eine Vorlage darf an dieser Schule genutzt werden, wenn sie keine Schulformen nennt oder die Schulform der Schule darunter ist. Eine unzulässige
	 * Vorlage wird ausgeblendet. Unabhängig von der Ausblendung lehnt der Server selbst eine Anfrage mit ungültiger Schulform ab.
	 *
	 * @param vorlage   die zu prüfende Reportvorlage
	 *
	 * @returns true, wenn die Vorlage an dieser Schule genutzt werden darf
	 */
	istSchulformZulaessig(vorlage: ReportingReportvorlage): boolean;

	/**
	 * Erzeuge die Parameter für die übergebene Vorlage, zusammengestellt aus neuen und gespeicherten Daten
	 *
	 * @param vorlage   die Vorlage, für die die Parameter erzeugt werden sollen
	 *
	 * @returns die Parameter
	 */
	createParameter(vorlage: ReportingReportvorlage): ReportingParameter;

}

export const ReportingStateKey: InjectionKey<ReportingState> = Symbol('ReportingState');

export function useReportingState(): ReportingState {
	const state = AppContext.instance.inject(ReportingStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des ReportingState über provide in der main.ts eingebunden");
	}
	return state;
}
