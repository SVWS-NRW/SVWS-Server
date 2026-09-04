import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeFaecher } from "./kataloge/faecher/RouteFaecher";
import { OpenApiError } from "@core/api/OpenApiError";
import { SMTPServerKonfiguration } from "@core/core/data/email/SMTPServerKonfiguration";
import { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";


interface RouteStateSchule extends RouteStateInterface {
	smtpServerKonfiguration: SMTPServerKonfiguration;
}

const defaultState = <RouteStateSchule> {
	smtpServerKonfiguration: new SMTPServerKonfiguration(),
	view: routeFaecher,
};

export class RouteDataSchule extends RouteData<RouteStateSchule> {

	public constructor() {
		super(defaultState);
	}

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData, mode: 'none' | 'schueler' | 'all'): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.setGostLupoImportMDBFuerJahrgang(formData, api.schema, mode);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response !== null) && ((e.response.status === 400) || (e.response.status === 409))) {
				const json: string = await e.response.text();
				return SimpleOperationResponse.transpilerFromJSON(json);
			}
			const result = new SimpleOperationResponse();
			result.log.add("Fehler bei der Server-Anfrage. ");
			if (e instanceof Error) {
				result.log.add("  " + e.message);
			}
			return result;
		}
	};

}
