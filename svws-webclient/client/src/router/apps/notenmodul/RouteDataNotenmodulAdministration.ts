import type { RouteParamsRawGeneric } from "vue-router";
import type { List, JavaMap, ENMServerConfigElement, ENMServerConnection, Abteilung } from "@core";
import { ENMConfigKlasse, ArrayList, UnsupportedOperationException, OpenApiError, DeveloperNotificationException, HashMap, SimpleOperationResponse, UserNotificationException, ENMConfigSpalte, ENMAbteilung } from "@core";
import { WenomAuswahlListeManager, ViewType } from "@ui";
import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeNotenmodulKonfiguration } from "./RouteNotenmodulKonfiguration";
import { routeNotenmodulKonfigurationNeu } from "./RouteNotenmodulKonfigurationNeu";
import { routeNotenmodulKonfigurationGruppenprozesse } from "./RouteNotenmodulGruppenprozesse";
import { routeNotenmodul } from "./RouteNotenmodul";
import { NotenmodulConfigManagerSperrungen } from "./NotenmodulConfigManagerSperrungen";
import { NotenmodulConfigManagerSichtbareSpalten } from "./NotenmodulConfigManagerSichtbareSpalten";


interface RouteStateNotenmodulAdministration extends RouteStateAuswahlInterface<WenomAuswahlListeManager> {
	mapInitialKennwoerter: JavaMap<number, string>;
	connected: boolean;
	mapENMServerConfigServer: JavaMap<string, string>;
	mapENMServerConfigGlobal: JavaMap<string, string>;
	mapAbteilungen: JavaMap<number, ENMAbteilung>;
	managerSperrungen: NotenmodulConfigManagerSperrungen;
	managerSichtbareSpalten: NotenmodulConfigManagerSichtbareSpalten;
}

export class RouteDataNotenmodulAdministration extends RouteDataAuswahl<WenomAuswahlListeManager, RouteStateNotenmodulAdministration> {

	public constructor() {
		super(<RouteStateNotenmodulAdministration>{
			idSchuljahresabschnitt: -1,
			manager: new WenomAuswahlListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
			view: routeNotenmodulKonfiguration,
			activeViewType: ViewType.DEFAULT,
			mapInitialKennwoerter: new HashMap<number, string>(),
			connected: false,
			mapENMServerConfigServer: new HashMap<string, string>(),
			mapENMServerConfigGlobal: new HashMap<string, string>(),
			mapAbteilungen: new HashMap<number, ENMAbteilung>(),
			managerSperrungen: new NotenmodulConfigManagerSperrungen(new ArrayList(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), async () => {}),
			managerSichtbareSpalten: new NotenmodulConfigManagerSichtbareSpalten(new ArrayList(), new HashMap(), async () => {}),
		}, { gruppenprozesse: routeNotenmodulKonfigurationGruppenprozesse, hinzufuegen: routeNotenmodulKonfigurationNeu });
	}

	public async entferneDaten() {
		this.setPatchedState({
			manager: undefined,
			mapInitialKennwoerter: new HashMap<number, string>(),
			connected: false,
			mapENMServerConfigServer: new HashMap<string, string>(),
			mapENMServerConfigGlobal: new HashMap<string, string>(),
			managerSperrungen: new NotenmodulConfigManagerSperrungen(new ArrayList(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), async () => {}),
			managerSichtbareSpalten: new NotenmodulConfigManagerSichtbareSpalten(new ArrayList(), new HashMap(), async () => {}),
		});
	}

	private createMapAbteilungen(listAbteilungen: List<Abteilung>) {
		const mapAbteilungen = new HashMap<number, ENMAbteilung>();
		for (const abteilung of listAbteilungen) {
			const enmAbteilung = new ENMAbteilung();
			enmAbteilung.id = abteilung.id;
			enmAbteilung.idAbteilungsleiter = abteilung.idAbteilungsleiter;
			enmAbteilung.bezeichnung = abteilung.bezeichnung;
			enmAbteilung.sortierung = abteilung.sortierung;
			for (const klasse of abteilung.klassenzuordnungen) {
				enmAbteilung.klassenzuordnungen.add(klasse.idKlasse);
			}
			mapAbteilungen.put(enmAbteilung.id, enmAbteilung);
		}
		return mapAbteilungen;
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number, isEntering: boolean): Promise<number | null> {
		const result = await super.setSchuljahresabschnitt(idSchuljahresabschnitt, isEntering);
		await routeNotenmodul.data.ladeDaten();
		const listAbteilungen = await api.server.getAbteilungenByIdJahresAbschnitt(api.schema, idSchuljahresabschnitt);
		const mapAbteilungen = this.createMapAbteilungen(listAbteilungen);
		const managerSperrungen = this.createSpaltenManager();
		const managerSichtbareSpalten = this.createManagerSichtbareSpalten();
		this.setPatchedState({ mapAbteilungen, managerSperrungen, managerSichtbareSpalten });
		const arr = [];
		for (const server of this.manager.filtered())
			arr.push(this.connect(server.id));
		await Promise.all(arr);
		return result;
	}

	protected async createManager(): Promise<Partial<RouteStateNotenmodulAdministration>> {
		const list = await api.server.getENMServerConnections(api.schema);
		const manager = new WenomAuswahlListeManager(api.schuleStammdaten.idSchuljahresabschnitt,
			api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, list);
		return { manager };
	}

	protected createManagerSichtbareSpalten(): NotenmodulConfigManagerSichtbareSpalten {
		const res = api.config.getValue("notenmodul.leistungen.tabelle.spalten.anzeige");
		const liste = new ArrayList<ENMConfigSpalte>();
		const configs: any[] | null = JSON.parse(res);
		if (configs !== null) {
			for (const config of configs) {
				const spalte = ENMConfigSpalte.transpilerFromJSON(JSON.stringify(config));
				liste.add(spalte);
			}
		}
		const mapTeilleistungsarten = routeNotenmodul.data.manager.mapTeilleistungsarten;
		return new NotenmodulConfigManagerSichtbareSpalten(liste, mapTeilleistungsarten, this.writeConfigSichtbareSpalten);
	}

	protected createSpaltenManager(): NotenmodulConfigManagerSperrungen {
		const res = api.config.getValue("notenmodul.leistungen.tabelle.spalten.readonly");
		const liste = new ArrayList<ENMConfigKlasse>();
		const configs: any[] | null = JSON.parse(res);
		if (configs !== null) {
			for (const config of configs) {
				const klasse = ENMConfigKlasse.transpilerFromJSON(JSON.stringify(config));
				liste.add(klasse);
			}
		}
		const { mapKlassen, mapTeilleistungsarten, mapJahrgaenge } = routeNotenmodul.data.manager;
		const mapAbteilungen = this._state.value.mapAbteilungen;
		return new NotenmodulConfigManagerSperrungen(liste, mapKlassen, mapTeilleistungsarten, mapJahrgaenge, mapAbteilungen, this.writeConfigSperrungen);
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: ENMServerConnection, state: Partial<RouteStateNotenmodulAdministration>): Promise<ENMServerConnection> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ENMServerConnection>, id: number): Promise<void> {
		await api.server.patchENMServerConnection(data, api.schema, id);
		this.commit();
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		for (const id of ids) {
			await api.server.deleteENMServerConnection(api.schema, id);
		}
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		return new ArrayList();
	}

	protected deleteMessage(id: number, eintrag: any): string {
		throw new UnsupportedOperationException("Die Methode ist nicht implementiert.");
	}

	get mapAbteilungen() {
		return this._state.value.mapAbteilungen;
	}

	public get manager(): WenomAuswahlListeManager {
		if (this._state.value.manager === undefined)
			throw new DeveloperNotificationException("Die ENM-Daten wurden nicht geladen.");
		return this._state.value.manager;
	}

	get mapEnmInitialKennwoerter(): JavaMap<number, string> {
		return this._state.value.mapInitialKennwoerter;
	}

	get connected(): boolean {
		return this._state.value.connected;
	}

	get mapEnmServerConfigServer(): JavaMap<string, string> {
		return this._state.value.mapENMServerConfigServer;
	}

	get managerSperrungen(): NotenmodulConfigManagerSperrungen {
		return this._state.value.managerSperrungen;
	}

	get managerSichtbareSpalten(): NotenmodulConfigManagerSichtbareSpalten {
		return this._state.value.managerSichtbareSpalten;
	}

	writeConfigSperrungen = async () => {
		const managerSperrungen = this.managerSperrungen;
		await api.config.setValue("notenmodul.leistungen.tabelle.spalten.readonly", managerSperrungen.json);
		this.setPatchedState({ managerSperrungen });
	};

	writeConfigSichtbareSpalten = async () => {
		const managerSichtbareSpalten = this.managerSichtbareSpalten;
		await api.config.setValue("notenmodul.leistungen.tabelle.spalten.anzeige", managerSichtbareSpalten.json);
		this.setPatchedState({ managerSichtbareSpalten });
	};

	connect = async (id: number): Promise<void> => {
		// Prüfe auf ein lokales Notenmodul mit einer negativer ID -1
		if (id < 0)
			return;
		const manager = this.manager;
		// Führe einen Verbindungstest für einen WeNoM-Server (externes Notenmodul) durch.
		// ... zunächst ein Wenom-Setup
		let result = await this.wenomSetup(id);
		if ((result instanceof SimpleOperationResponse) && (result.id === null)) {
			manager.setConnectionResponse(id, result);
			this.setPatchedState({ manager });
			return;
		}

		// ... Führe die eigentlich Überprüfung der Verbindung durch
		result = await this.wenomCheck(id);
		manager.setConnectionResponse(id, result);
		this.setPatchedState({ manager });
	};

	trustCertificate = async (serverTLSCertIsTrusted: boolean): Promise<void> => {
		try {
			const manager = this.manager;
			await api.server.patchENMServerConnection({ serverTLSCertIsTrusted }, api.schema, manager.auswahl().id);
			manager.auswahl().serverTLSCertIsTrusted = serverTLSCertIsTrusted;
			await this.connect(manager.auswahl().id);
		} catch {
			throw new UserNotificationException("Konnte nicht Setzen, dass dem Zertifikat vertraut wird.");
		}
	};

	wenomGetEnmCredentials = async (): Promise<void> => {
		try {
			const daten = await api.server.getENMLehrerInitialKennwoerter(api.schema);
			const mapInitialKennwoerter = new HashMap<number, string>();
			for (const eintrag of daten)
				if (eintrag.initialKennwort !== null)
					mapInitialKennwoerter.put(eintrag.id, eintrag.initialKennwort);
			this.setPatchedState({ mapInitialKennwoerter });
		} catch {
			return;
		}
	};

	wenomAddCredentials = async (data: Partial<ENMServerConnection>): Promise<void> => {
		const manager = this.manager;
		try {
			const server = await api.server.addENMServerConnection(data, api.schema);
			manager.liste.add(server);
			await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
			await this.gotoDefaultView(server.id);
		} catch { /** */ }
	};

	wenomUpdateServerConnection = async (data: Partial<ENMServerConnection>): Promise<void> => {
		const manager = this.manager;
		await api.server.patchENMServerConnection(data, api.schema, manager.auswahl().id);
		const auswahl = manager.daten();
		Object.assign(auswahl, data);
		manager.setDaten(auswahl);
		this.setPatchedState({ manager });
	};

	wenomGetServerConfig = api.call(async (): Promise<void> => {
		try {
			const res = await api.server.getENMServerConfig(api.schema, this.manager.auswahl().id);
			if (res.success && (res.config !== null)) {
				const mapENMServerConfigGlobal = new HashMap<string, string>();
				const mapENMServerConfigServer = new HashMap<string, string>();
				for (const element of res.config.global)
					mapENMServerConfigGlobal.put(element.key, element.value);
				for (const element of res.config.server)
					mapENMServerConfigServer.put(element.key, element.value);
				this.setPatchedState({ mapENMServerConfigGlobal, mapENMServerConfigServer });
			} else
				throw new DeveloperNotificationException("Keine Konfiguration geladen");
		} catch {
			return;
		}
	});

	wenomSetServerConfigElement = api.call(async (data: ENMServerConfigElement): Promise <SimpleOperationResponse> => {
		try {
			const res = await api.server.setENMServerConfigElement(data, api.schema, this.manager.auswahl().id);
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
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Setzen einer Serverkonfiguration aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomCheck = api.call(async (id?: number): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.checkENMServer(api.schema, id ?? this.manager.auswahl().id);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Checkmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomSetup = api.call(async (id?: number): Promise<boolean | SimpleOperationResponse> => {
		try {
			return await api.server.setupENMServer(api.schema, id ?? this.manager.auswahl().id);
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
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Setupmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomSynchronize = api.call(async (): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.synchronizeENMDaten(api.schema, this.manager.auswahl().id);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Synchronisationsmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomDownload = api.call(async (): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.downloadENMDaten(api.schema, this.manager.auswahl().id);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Downloadmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomUpload = api.call(async (): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.uploadENMDaten(api.schema, this.manager.auswahl().id);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Uploadmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomTruncate = api.call(async (): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.truncateENMServer(api.schema, this.manager.auswahl().id);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Truncatemethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});

	wenomReset = api.call(async (): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.resetENMServer(api.schema, this.manager.auswahl().id);
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response)) {
				try {
					const json = await e.response.text();
					return SimpleOperationResponse.transpilerFromJSON(json);
				} catch { /* */ }
			}
			const res = new SimpleOperationResponse();
			res.success = false;
			res.log.add(`Unerwarteter Fehler beim Aufruf der Resetmethode aufgetreten: ${e instanceof Error ? e.message : 'unbekannt'}`);
			return res;
		}
	});
}
