import type { ENMServerConfigElement, JavaMap } from "@core";
import { ENMDaten, OAuth2ClientConnection, OAuth2ServerTyp, OpenApiError, SimpleOperationResponse, HashMap, DeveloperNotificationException, UserNotificationException } from "@core";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";


interface RouteStateSchuleDatenaustauschWenom extends RouteStateInterface {
	connected: boolean;
	connectionInfo : OAuth2ClientConnection | null;
	enmDaten: ENMDaten | null;
	mapInitialKennwoerter: JavaMap<number, string>;
	mapENMServerConfigServer: JavaMap<string, string>;
	mapENMServerConfigGlobal: JavaMap<string, string>;
}

const defaultState = <RouteStateSchuleDatenaustauschWenom> {
	connected: false,
	connectionInfo: null,
	enmDaten: null,
	mapInitialKennwoerter: new HashMap<number, string>(),
	mapENMServerConfigServer: new HashMap<string, string>(),
	mapENMServerConfigGlobal: new HashMap<string, string>(),
};

export class RouteDataSchuleDatenaustauschWenom extends RouteData<RouteStateSchuleDatenaustauschWenom> {

	public constructor() {
		super(defaultState);
	}

	get connected(): boolean {
		return this._state.value.connected;
	}

	get connectionInfo(): OAuth2ClientConnection | null {
		return this._state.value.connectionInfo;
	}

	get enmDaten() : ENMDaten | null {
		return this._state.value.enmDaten;
	}

	get mapEnmInitialKennwoerter(): JavaMap<number, string> {
		return this._state.value.mapInitialKennwoerter;
	}

	// TODO korrektes Laden der Server-Konfiguration, immer dann, wenn ein check erfolgreich war: await this.wenomGetServerConfig();
	get mapEnmServerConfigServer(): JavaMap<string, string> {
		return this._state.value.mapENMServerConfigServer;
	}

	/**
	 * Initialisiert die Daten der Route. Wird beim Betreten der Ansicht ausgeführt.
	 */
	public async init() {
		// Bestimme die Verbindungsinformationen zum ENM-Server, ...
		const connectionInfo = await this.getConnectionInfo();
		// ... laden die ENM-Daten, ...
		const enmDaten = await this.getEnmDaten();
		// ... lese die Liste mit den Initialkennwörtern der Lehrer aus den ENM-Daten ein ...
		const mapInitialKennwoerter = await this.getEnmLehrerInitialKennwoerter();
		// ... und aktualisiere den State
		this.setPatchedState({ connectionInfo, enmDaten, mapInitialKennwoerter });
	}


	private async getConnectionInfo() : Promise<OAuth2ClientConnection | null> {
		try {
			return await api.server.getOAuthClientSecret(api.schema, 1);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				if (e.response.status === 404) {
					try {
						const data = new OAuth2ClientConnection();
						data.id = OAuth2ServerTyp.WENOM.getId();
						data.clientID = OAuth2ServerTyp.WENOM.getId().toString();
						return await api.server.addOAuthClientSecret(data, api.schema);
					} catch {
						return null;
					}
				}
			}
			return null;
		}
	}

	private async getEnmDaten() : Promise<ENMDaten | null> {
		try {
			const datenGzip = await api.server.getENMDatenGZip(api.schema);
			const datenBlob = await new Response(datenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
			return ENMDaten.transpilerFromJSON(await datenBlob.text());
		} catch (e) {
			return null;
		}
	}

	private async getEnmLehrerInitialKennwoerter() : Promise<JavaMap<number, string>> {
		const result = new HashMap<number, string>();
		try {
			const daten = await api.server.getENMLehrerInitialKennwoerter(api.schema);
			for (const eintrag of daten)
				if (eintrag.initialKennwort !== null)
					result.put(eintrag.id, eintrag.initialKennwort);
			this.setPatchedState({ mapInitialKennwoerter: result });
		} catch (e) {
			result.clear();
		}
		return result;
	}

	connect = async (): Promise<SimpleOperationResponse> => {
		// Führe ein Wenom-Setup aus. Ist dieser Aufruf erfolgreich
		const resultSetup = await this.wenomSetup();
		if ((resultSetup instanceof SimpleOperationResponse) && (resultSetup.id === null))
			return resultSetup;
		// Lade die Informationen zur Verbindung erneut
		const connectionInfo = await this.getConnectionInfo();
		if ((resultSetup instanceof SimpleOperationResponse) && (resultSetup.id === 409)) {
			this.setPatchedState({ connectionInfo, connected: false });
			return resultSetup;
		}
		// Führe die eigentlich Überprüfung der Verbindung durch
		const result = await this.wenomCheck();
		this.setPatchedState({ connectionInfo, connected: result.success });
		return result;
	}

	trustCertificate = async (tlsCertIsTrusted : boolean): Promise<void> => {
		if (this.connectionInfo === null)
			return;
		try {
			const connectionInfo = this.connectionInfo;
			await api.server.patchOAuthSecret({ tlsCertIsTrusted }, api.schema, connectionInfo.id);
			connectionInfo.tlsCertIsTrusted = tlsCertIsTrusted;
			this.setPatchedDefaultState({ connectionInfo });
		} catch (error) {
			throw new UserNotificationException("Konnte nicht Setzen, dass dem Zertifikat vertraut wird.");
		}
	}

	wenomGetEnmCredentials = async(): Promise<void> => {
		try {
			const daten = await api.server.getENMLehrerInitialKennwoerter(api.schema);
			const mapInitialKennwoerter = new HashMap<number, string>();
			for (const eintrag of daten)
				if (eintrag.initialKennwort !== null)
					mapInitialKennwoerter.put(eintrag.id, eintrag.initialKennwort);
			this.setPatchedState({ mapInitialKennwoerter });
		} catch (e) {
			return;
		}
	}

	wenomSetCredentials = async (url: string, token: string): Promise<boolean> => {
		if (this.connectionInfo === null)
			return false;
		const patch = <Partial<OAuth2ClientConnection>>{
			authServer: url,
			clientSecret: token,
		};
		const id = OAuth2ServerTyp.WENOM.getId();
		try {
			await api.server.patchOAuthSecret(patch, api.schema, id);
		} catch (e) {
			return false;
		}
		this.setPatchedDefaultState({ connectionInfo: Object.assign(patch, this.connectionInfo) });
		return true;
	}

	wenomPatchCredentialsSecret = async (clientSecret: string): Promise<void> => {
		const wenom = OAuth2ServerTyp.WENOM;
		try {
			return await api.server.patchOAuthSecret({ clientSecret }, api.schema, wenom.getId());
		} catch (e) {
			return;
		}
	}

	wenomRemoveCredential = api.call(async () => {
		await api.server.deleteOAuthSecret(api.schema, 1);
		// Aktualisiere die Verbindungsinformationen zum ENM-Server und setze den State neu
		const connectionInfo = await this.getConnectionInfo();
		this.setPatchedState({ connectionInfo, enmDaten: this._state.value.enmDaten, mapInitialKennwoerter: this._state.value.mapInitialKennwoerter });
	});

	wenomGetServerConfig = api.call(async (): Promise<void> => {
		try {
			const res = await api.server.getENMServerConfig(api.schema);
			if (res.success && (res.config !== null)) {
				const mapENMServerConfigGlobal = new HashMap<string, string>();
				const mapENMServerConfigServer = new HashMap<string, string>();
				for (const element of res.config.global)
					mapENMServerConfigGlobal.put(element.key, element.value);
				for (const element of res.config.server)
					mapENMServerConfigServer.put(element.key, element.value);
				this.setPatchedState({ mapENMServerConfigGlobal, mapENMServerConfigServer});
			} else
				throw new DeveloperNotificationException("Keine Konfiguration geladen");
		} catch (error) {
			return;
		}
	})

	wenomSetServerConfigElement = api.call(async (data: ENMServerConfigElement): Promise <SimpleOperationResponse> => {
		try {
			const res = await api.server.setENMServerConfigElement(data, api.schema);
			if (data.type === 'server')
				this._state.value.mapENMServerConfigServer.put(data.key, data.value);
			else
				this._state.value.mapENMServerConfigGlobal.put(data.key, data.value);
			return res;
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Setzen einer Serverkonfiguration aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	})

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

	wenomSetup = api.call(async () : Promise<boolean | SimpleOperationResponse> => {
		try {
			return (await api.server.setupENMServer(api.schema))!;
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				if (e.response.status === 409) {
					const res = new SimpleOperationResponse();
					res.id = 409;
					res.success = false;
					res.log.add('Dem Server-Zertifikat wird aktuell nicht vertraut.');
					return res;
				}
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch (e) { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Setupmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

}
