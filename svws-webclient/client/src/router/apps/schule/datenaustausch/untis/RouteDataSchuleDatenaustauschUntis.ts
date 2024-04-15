import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchuleDatenaustauschUntisStundenplan } from "./RouteSchuleDatenaustauschUntisStundenplan";

import { OpenApiError, SimpleOperationResponse } from "@core";


interface RouteStateDatenaustauschUntis extends RouteStateInterface {
}

const defaultState = <RouteStateDatenaustauschUntis> {
	view: routeSchuleDatenaustauschUntisStundenplan
};


export class RouteDataSchuleDatenaustauschUntis extends RouteData<RouteStateDatenaustauschUntis> {

	public constructor() {
		super(defaultState);
	}

	public async importUntisGPU(apimethod : () => Promise<SimpleOperationResponse>) : Promise<SimpleOperationResponse> {
		try {
			return await apimethod();
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response !== null) && ((e.response.status === 400) || (e.response.status === 404) || (e.response.status === 409) || (e.response.status === 500))) {
				const json : string = await e.response.text();
				return SimpleOperationResponse.transpilerFromJSON(json);
			}
			const result = new SimpleOperationResponse();
			result.log.add("Fehler bei der Server-Anfrage. ");
			if (e instanceof Error)
				result.log.add("  " + e.message);
			return result;
		}
	}

	importUntisStundenplanGPU001 = async (formData: FormData) : Promise<SimpleOperationResponse> => {
		return await this.importUntisGPU(() => api.server.importStundenplanUntisGPU001(formData, api.schema));
	}

	importUntisRaeumeGPU005 = async (formData: FormData) : Promise<SimpleOperationResponse> => {
		return await this.importUntisGPU(() => api.server.importUntisRaeumeGPU005(formData, api.schema));
	}

}

