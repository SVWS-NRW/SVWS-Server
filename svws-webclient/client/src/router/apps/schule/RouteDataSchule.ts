import { BenutzerKompetenz, OAuth2ClientSecret, OAuth2ServerTyp, OpenApiError, SimpleOperationResponse, SMTPServerKonfiguration, type SchuleStammdaten } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuleFaecher } from "./faecher/RouteSchuleFaecher";
import { AES } from "~/utils/crypto/aes";
import { AESAlgo } from "~/utils/crypto/aesAlgo";


interface RouteStateSchule extends RouteStateInterface {
	smtpServerKonfiguration: SMTPServerKonfiguration;
}

const defaultState = <RouteStateSchule> {
	smtpServerKonfiguration: new SMTPServerKonfiguration(),
	view: routeSchuleFaecher,
};

export class RouteDataSchule extends RouteData<RouteStateSchule> {

	public constructor() {
		super(defaultState);
	}

	patch = async (data : Partial<SchuleStammdaten>) => {
		const stammdaten = api.schuleStammdaten;
		await api.server.patchSchuleStammdaten(data, api.schema);
		Object.assign(stammdaten, data);
		api.updatedApiData();
		this.commit();
	}

	public async ladeDaten() {
		let smtpServerKonfiguration = new SMTPServerKonfiguration();
		if (api.benutzerIstAdmin || api.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN))
			smtpServerKonfiguration = await api.server.getSMTPServerKonfiguration(api.schema);
		this.setPatchedState({ smtpServerKonfiguration });
	}

	public async entferneDaten() {
		const smtpServerKonfiguration = new SMTPServerKonfiguration();
		this.setPatchedState({ smtpServerKonfiguration });
	}

	get smtpServerKonfiguration(): SMTPServerKonfiguration {
		return this._state.value.smtpServerKonfiguration;
	}

	patchSMTServerKonfiguration = async (data : Partial<SMTPServerKonfiguration>) => {
		const smtpServerKonfiguration = this._state.value.smtpServerKonfiguration;
		await api.server.patchSMTPServerKonfiguration(data, api.schema);
		Object.assign(smtpServerKonfiguration, data);
		this.setPatchedState({ smtpServerKonfiguration });
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

	wenomGetCredentials = async (): Promise<OAuth2ClientSecret | null> => {
		try {
			return await api.server.getOAuthClientSecret(api.schema,1);
		} catch (e) {
			return null;
		}
	}

	wenomSetCredentials = async (url: string, token: string): Promise<OAuth2ClientSecret | null> => {
		const wenom = OAuth2ServerTyp.WENOM;
		const oauth = new OAuth2ClientSecret();
		oauth.id = wenom.getId();
		oauth.clientID = "1";
		oauth.authServer = url;
		oauth.clientSecret = token;
		try {
			return await api.server.addOAuthClientSecret(oauth, api.schema);
		} catch (e) {
			return null;
		}
	}

	wenomRemoveCredential = api.call(async () => {
		await api.server.deleteOAuthSecret(api.schema, 1);
	});

	wenomSynchronize = api.call(async () : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.synchronizeENMDaten(api.schema);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Synchronisationsmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomDownload = api.call(async () : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.downloadENMDaten(api.schema);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Downloadmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomUpload = api.call(async () : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.uploadENMDaten(api.schema);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Uploadmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomTruncate = api.call(async () : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.truncateENMServer(api.schema);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Truncatemethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomReset = api.call(async () : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.resetENMServer(api.schema);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Resetmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomCheck = api.call(async () : Promise<SimpleOperationResponse> => {
		try {
			return await api.server.checkENMServer(api.schema);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Checkmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

}
