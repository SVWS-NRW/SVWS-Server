import { ReportingAusgabeformat, ReportingParameter, type ApiFile, type SimpleOperationResponse } from "@core";
import type { ReportingState } from "@ui";
import { StateManager } from "@ui";
import { api } from "~/router/Api";
import { abschnittState } from "./AbschnittStateImpl";

interface ReportingReactiveState {
}

export class ReportingStateImpl extends StateManager<ReportingReactiveState> implements ReportingState {

	public constructor() {
		super({});
	}

	public async fetchEMailJobStatus(jobId: number): Promise<SimpleOperationResponse> {
		return await api.server.getEmailJobStatus(api.schema, jobId);
	}

	public async fetchEMailJobLog(jobId: number): Promise<SimpleOperationResponse> {
		return await api.server.getEmailJobLog(api.schema, jobId);
	}

	public createPDFReport = async (reportingParameter: ReportingParameter): Promise<void> => {
		reportingParameter.ausgabeformat = ReportingAusgabeformat.PDF.getId();
		reportingParameter.idSchuljahresabschnitt = abschnittState.auswahl.id;
		api.status.start();
		try {
			const apiFile = await api.server.pdfReport(reportingParameter, api.schema);
			await this.downloadApiFile(apiFile);
		} finally {
			api.status.stop();
		}
	};

	public createEMailReport = async (reportingParameter: ReportingParameter): Promise<SimpleOperationResponse> => {
		reportingParameter.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();
		reportingParameter.idSchuljahresabschnitt = abschnittState.auswahl.id;
		api.status.start();
		try {
			return await api.server.emailReport(reportingParameter, api.schema);
		} finally {
			api.status.stop();
		}
	};

	public createHTMLReport = async (reportingParameter: ReportingParameter): Promise<string> => {
		reportingParameter.ausgabeformat = ReportingAusgabeformat.HTML.getId();
		reportingParameter.idSchuljahresabschnitt = abschnittState.auswahl.id;
		api.status.start();
		try {
			return await api.server.htmlReport(reportingParameter, api.schema);
		} finally {
			api.status.stop();
		}
	};

	public createJSONReportingParameter = async (reportingParameter: ReportingParameter): Promise<void> => {
		const json = ReportingParameter.transpilerToJSON(reportingParameter);
		const data = new Blob([json], { type: "application/json" });
		const apiFile = { name: "ExportReportingParameter.json", data };
		await this.downloadApiFile(apiFile);
	};

	private async downloadApiFile(apiFile: ApiFile) {
		const { data, name } = apiFile;
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}


}

export const reportingState = new ReportingStateImpl();
