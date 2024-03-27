import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchuleDatenaustauschKurs42Blockung } from "./RouteSchuleDatenaustauschKurs42Blockung";

import { OpenApiError, SimpleOperationResponse } from "@core";


interface RouteStateDatenaustauschKurs42 extends RouteStateInterface {
}

const defaultState = <RouteStateDatenaustauschKurs42> {
	view: routeSchuleDatenaustauschKurs42Blockung
};


export class RouteDataSchuleDatenaustauschKurs42 extends RouteData<RouteStateDatenaustauschKurs42> {

	public constructor() {
		super(defaultState);
	}

	setGostKurs42ImportZip = async (formData: FormData) : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.importKurs42Blockung(formData, api.schema);
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

}

