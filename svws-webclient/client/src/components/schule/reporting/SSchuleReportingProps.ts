import type { ApiFile, ReportingParameter } from "@core";

export interface SchuleReportingProps {
	idAbschnitt: number;
	createReport: (param: ReportingParameter) => Promise<ApiFile>;
}