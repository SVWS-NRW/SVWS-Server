import { AES } from "~/utils/crypto/aes";
import { AESAlgo } from "~/utils/crypto/aesAlgo";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";
import { routeSchuleDatenaustauschLaufbahnplanung } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustauschLupo";

import { OpenApiError, SimpleOperationResponse } from "@core";


interface RouteStateDatenaustausch extends RouteStateInterface {
}

const defaultState = <RouteStateDatenaustausch> {
	view: routeSchuleDatenaustauschLaufbahnplanung
};


export class RouteDataSchuleDatenaustausch extends RouteData<RouteStateDatenaustausch> {

	public constructor() {
		super(defaultState);
	}

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData, mode: 'none' | 'schueler' | 'all') : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.setGostLupoImportMDBFuerJahrgang(formData, api.schema, mode);
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response != null) && ((e.response.status === 400) || (e.response.status === 409))) {
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

	setGostKurs42ImportZip = async (formData: FormData) : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.importKurs42Blockung(formData, api.schema);
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response != null) && ((e.response.status === 400) || (e.response.status === 404) || (e.response.status === 409) || (e.response.status === 500))) {
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

	setImportENM = async (file: File, password: string, salt: string) => {
		const key = await AES.getKey256(password, salt);
		const aes = new AES(AESAlgo.CBC, key);
		const base64 = new TextDecoder().decode(await file.arrayBuffer());
		const encoded = await aes.decryptBase64(base64);
		console.log(new TextDecoder().decode(encoded));
		return true;
	}

	setUntisImportGPU = async (formData: FormData) => {
		try {
			return await api.server.importStundenplanUntisGPU001(formData, api.schema)
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response != null) && ((e.response.status === 400) || (e.response.status === 409))) {
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

