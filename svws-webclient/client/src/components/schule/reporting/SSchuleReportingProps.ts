import type { ApiFile, ReportingParameter, Schuljahresabschnitt } from "@core";

export interface SchuleReportingProps {
	schuljahresabschnitt: () => Schuljahresabschnitt;
	createReport: (param: ReportingParameter) => Promise<ApiFile>;
	createHtmlPreview: (param: ReportingParameter) => Promise<string>;
}
