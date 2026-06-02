import { inject, type InjectionKey } from "vue";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import type { ReportingParameter } from "../../../core/src/core/data/reporting/ReportingParameter";
import type { SimpleOperationResponse } from "../../../core/src/core/data/SimpleOperationResponse";

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
}

export const ReportingStateKey: InjectionKey<ReportingState> = Symbol('ReportingState');

export function useReportingState(): ReportingState {
	const state = inject(ReportingStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des ReportingState über provide in der main.ts eingebunden");
	}
	return state;
}
