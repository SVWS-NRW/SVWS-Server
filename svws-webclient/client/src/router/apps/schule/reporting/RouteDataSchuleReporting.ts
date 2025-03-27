import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import type { ApiFile, ReportingParameter } from "@core";

const defaultState = <RouteStateInterface> {
};


export class RouteDataSchuleReporting extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

	createReport = async (param: ReportingParameter): Promise<ApiFile> => {
		return await api.server.pdfReport(param, api.schema);
	}

}
