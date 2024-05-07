import { AES } from "~/utils/crypto/aes";
import { AESAlgo } from "~/utils/crypto/aesAlgo";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchuleDatenaustauschLaufbahnplanung } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustauschLupo";

import { OAuth2ClientSecret, OAuth2ServerTyp, OpenApiError, SimpleOperationResponse } from "@core";


interface RouteStateDatenaustausch extends RouteStateInterface {
	secretSet: boolean;
}

const defaultState = <RouteStateDatenaustausch> {
	secretSet: false,
	view: routeSchuleDatenaustauschLaufbahnplanung,
};


export class RouteDataSchuleDatenaustausch extends RouteData<RouteStateDatenaustausch> {

	public constructor() {
		super(defaultState);
	}

	public get secretSet() : boolean {
		return this._state.value.secretSet;
	}

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData, mode: 'none' | 'schueler' | 'all') : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.setGostLupoImportMDBFuerJahrgang(formData, api.schema, mode);
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response !== null) && ((e.response.status === 400) || (e.response.status === 409))) {
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

	ladeCredentials = async () => {
		let res;
		try {
			res = await api.server.getOAuthClientSecret(api.schema,1);
		} catch {
			console.log("Kein OAuth-Secret vorhanden.");
		}
		this.setPatchedState({secretSet: res !== undefined});
	}

	setWenomCredentials = async (url: string, token: string) => {
		const wenom = OAuth2ServerTyp.WENOM;
		const oauth = new OAuth2ClientSecret();
		oauth.id = wenom.getId();
		oauth.clientID = "1";
		oauth.authServer = url;
		oauth.clientSecret = token;
		await api.server.addOAuthClientSecret(oauth, api.schema);
		this.setPatchedState({secretSet: true});
	}

	wenomSynchronize = api.call(async () => {await api.server.synchronizeENMDaten(api.schema)});

	wenomTruncate = api.call(async () => {await api.server.truncateENMServer(api.schema)});

	wenomRemoveCredential = api.call(async () => {
		await api.server.deleteOAuthSecret(api.schema, 1);
		this.setPatchedState({secretSet: false});
	});

}

