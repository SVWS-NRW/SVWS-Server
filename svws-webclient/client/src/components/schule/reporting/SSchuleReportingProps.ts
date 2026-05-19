import type { ApiFile, ReportingParameter } from "@core";

export interface SchuleReportingProps {
	createReport: (param: ReportingParameter) => Promise<ApiFile>;
	createHtmlPreview: (param: ReportingParameter) => Promise<string>;
}
