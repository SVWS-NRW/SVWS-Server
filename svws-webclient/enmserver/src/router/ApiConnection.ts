import { ENMDaten } from "@core/core/data/enm/ENMDaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { ref, shallowRef } from "vue";
import { ApiEnmServer } from "~/ApiEnmServer";
import { Config } from "~/components/Config";
import { EnmManager } from "~/components/leistungen/EnmManager";

export class ApiConnection {

	// Gibt an, ob der Client beim Server authentifiziert ist
	protected _authenticated = ref<boolean>(false);

	// Der Hostname (evtl. mit Port) des Servers, bei dem der Login stattfindet
	protected _hostname = ref<string>(window.location.hostname + ":" + window.location.port);

	// Die URL mit welcher der Server verbunden ist
	protected _url: string | undefined = undefined;

	// Der Benutzername für den Login
	protected _username = "";

	// Das Kennwort für den Login
	protected _password = "";

	// Der Modus, in welchem der Server betrieben wird
	protected _serverMode = shallowRef<ServerMode>(ServerMode.DEV)

	// Die Api selbst
	protected _api: ApiEnmServer | undefined;

	// Die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	protected _daten: ENMDaten | undefined = undefined;

	// Der Manager für die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	protected _manager: EnmManager | undefined = undefined;

	// Die aktuelle Konfiguration der Schule, sofern ein Login stattgefunden hat
	protected _config = ref<Config | undefined>(undefined);

	// Die aktuelle temporäre, nicht in der DB festgehaltene Konfiguration der Schule
	protected _nonPersistentConfig = ref<Config>(new Config(async (_,__) => {}, async (_,__) => { }));


	/**
	 * Erstellt ein neues Objekt für die Verwaltung der API-Verbindung.
	 */
	public constructor() {
		this._config.value = new Config(this.setConfigGlobal, this.setConfigUser);
	}

	// Gibt die Server-API zurück.
	get api(): ApiEnmServer {
		if (this._api === undefined)
			throw new DeveloperNotificationException("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen")
		return this._api;
	}

	// Gibt den Hostname zurück
	get hostname() : string {
		return this._hostname.value;
	}

	// Gibt den Status zurück, ob der Benutzer authentifiziert wurde
	get authenticated() : boolean {
		return this._authenticated.value;
	}

	// Gibt den Benutzernamen zurück
	get username() : string {
		return this._username;
	}

	/**
	 * Stellt eine Verbindung zu dem angebenen Hostnamen her.
	 *
	 * @param hostname   der Hostname, evtl. mit Port-Adresse
	 */
	protected async connect(hostname : string): Promise<void> {
		const url = `https://${hostname}`;
		const api = new ApiEnmServer(url, "", "");
		await api.isAlive();
		this._hostname.value = hostname;
		this._url = url;
	}

	/**
	 * Setzt den Hostnamen, der für die Verbindung verwendet wird.
	 *
	 * @param hostname    der Hostname
	 */
	setHostname = (hostname: string): void => {
		this._hostname.value = hostname;
	}

	/**
	 * Versucht eine Verbindung zu dem ENM-Server mit dem angegebenen Hostnamen aufzubauen.
	 *
	 * @param {string} name Der Hostname unter der der ENM-Server erreichbar sein soll
	 */
	connectTo = async (name: string): Promise<void> => {
		const url = new URL('https://' + name);
		const host = url.host;
		console.log(`Verbinde zum ENM-Server unter https://${host}...`);
		try {
			await this.connect(host);
			if (url.port.length > 0)
				localStorage.setItem("ENM-Server Port", url.port);
			return;
		} catch (error) {
			console.log(`Verbindung zum ENM-Server unter https://${host} fehlgeschlagen`);
		}
		const hostname = url.hostname;
		if (host !== hostname) {
			console.log(`Verbinde zum ENM-Server unter https://${hostname}...`);
			try {
				await this.connect(hostname);
				return;
			} catch (error) {
				console.log(`Verbindung zum ENM-Server unter https://${hostname} fehlgeschlagen.`);
			}
		}
		const port = localStorage.getItem("ENM-Server Port");
		if ((port !== null) && (port !== url.port)) {
			console.log(`Verbinde zum ENM-Server unter https://${hostname}:${port}...`);
			try {
				await this.connect(`${hostname}:${port}`);
				return;
			} catch (error) {
				console.log(`Verbindung zum ENM-Server unter https://${hostname}:${port} fehlgeschlagen.`);
			}
		}
		throw new UserNotificationException('Es konnte keine Verbindung hergestellt werden.');
	}

	/**
	 * Authentifiziert den angebenen Benutzer mit dem angegebenen Kennwort.
	 *
	 * @param {string} username   der Benutzername
	 * @param {string} password   das Kennwort
	 *
	 * @returns {Promise<boolean>}
	 */
	login = async (username: string, password: string): Promise<boolean> => {
		try {
			if (this._url === undefined)
				throw new DeveloperNotificationException("Keine gültige URL für einen Login verfügbar.");
			this._username = username;
			this._password = password;
			this._api = new ApiEnmServer(this._url, this._username, this._password);
			// Prüfe, ob die Anmeldedaten korrekt sind, in dem die ENM-Daten geladen werden
			const file = await this._api.getLehrerENMDaten();
			const blob = await new Response(file.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
			this._daten = ENMDaten.transpilerFromJSON(await blob.text());
			this._manager = new EnmManager(this._daten, this._daten.lehrerID ?? -1);
			this._authenticated.value = true;
			await this.initConfig();
		} catch (error) {
			// TODO Anmelde-Fehler wird nur in der App angezeigt. Der konkreten Fehler könnte ggf. geloggt werden...
			this._authenticated.value = false;
			this._daten = undefined;
			this._manager = undefined;
		}
		return this._authenticated.value;
	}

	/**
	 * Liest die Client-Konfiguration vom Server und erstellt das zugehörige
	 * TypeScript-Objekt.
	 *
	 * @returns das Konfigurationsobjekt
	 */
	protected async initConfig(): Promise<void> {
		const cfg = await this.api.getClientConfig();
		const mapUser = new Map<string, string>();
		for (const c of cfg.user)
			mapUser.set(c.key, c.value);
		const mapGlobal = new Map<string, string>();
		for (const c of cfg.global)
			mapGlobal.set(c.key, c.value);
		this.config.mapGlobal = mapGlobal;
		this.config.mapUser = mapUser;
		// nicht-persistente Config ebenfalls anlegen
		this.nonPersistentConfig.mapGlobal = new Map<string, string>();
		this.nonPersistentConfig.mapUser = new Map<string, string>();
	}

	/**
	 * Initialialisiert die Daten, die beim Login geladen werden sollen
	 *
	 * @returns {Promise<boolean>} true beim erfolgreichen Laden der Daten und ansonsten false
	 */
	init = async (): Promise<boolean> => {
		return true;
	}

	/**
	 * Trennt die Verbindung für den aktuell angemeldeten Benutzer
	 */
	logout = async (): Promise<void> => {
		this._authenticated.value = false;
		this._username = "";
		this._password = "";
		this._api = undefined;
		this._daten = undefined;
		this._manager = undefined;
	}

	// Gibt den Modus zurück, in welchem der Server betrieben wird.
	get mode(): ServerMode {
		return this._serverMode.value;
	}

	// Gibt die ENM-Daten zurück
	get daten(): ENMDaten {
		if (this._daten === undefined)
			throw new DeveloperNotificationException("Es wurden noch keine ENM-Daten geladen - Ein Zugriff auf diese Methode darf daher zu diesem Zeitpunkt nicht erfolgen")
		return this._daten;
	}

	// Gibt den Manager für die ENM-Daten des angemeldeten Benutzers zurück
	get manager(): EnmManager {
		if (this._manager === undefined)
			throw new DeveloperNotificationException("Es wurden noch keine ENM-Daten geladen - Ein Zugriff auf diese Methode darf daher zu diesem Zeitpunkt nicht erfolgen")
		return this._manager;
	}

	// Gibt die Konfiguration für den angemeldeten Benutzer zurück, sofern ein Login stattgefunden hat
	get config(): Config {
		if (this._config.value === undefined)
			throw new DeveloperNotificationException("Eine Konfiguration ist nicht vorhanden.");
		return this._config.value as Config;
	}

	// Gibt die nicht perisstente Konfiguration zurück
	get nonPersistentConfig(): Config {
		return this._nonPersistentConfig.value as Config;
	}

	/**
	 * Setzt den Benutzer-spezifischen Konfigurationseintrag
	 *
	 * @param key    der Schlüssel des Konfigurationseintrags
	 * @param value  der Wert des Konfigurationseintrags
	 */
	protected setConfigUser = async (key: string, value: string): Promise<void> => {
		await this.api.setClientConfigUserKey(value, key);
	}

	/**
	 * Setzt den globalen Konfigurationseintrag
	 *
	 * @param key    der Schlüssel des Konfigurationseintrags
	 * @param value  der Wert des Konfigurationseintrags
	 */
	protected setConfigGlobal = async (key: string, value: string): Promise<void> => {
		throw new DeveloperNotificationException("Die Anwendung unterstützt kein Schreiben der globalen Konfiguration.");
	}

}

