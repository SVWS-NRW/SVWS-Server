import { ref, shallowRef } from "vue";

import type { BenutzerDaten, DBSchemaListeEintrag, List, SchuleStammdaten } from "@core";
import { ValidatorKontext, ApiSchema, ApiServer, ApiExternal, BenutzerKompetenz, ServerMode, DeveloperNotificationException, UserNotificationException, OpenApiError } from "@core";

import { Config } from "../../../ui/src/utils/Config";
import { AES } from "~/utils/crypto/aes";
import { AESAlgo } from "~/utils/crypto/aesAlgo";

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

	// Das benutzerspezifische AES-Objekt zur Verschlüsselung
	protected _aes = shallowRef<AES | undefined>(undefined);

	// Der Name des Schemas auf dem SVWS-Server, bei dem der Login stattfindet
	protected _schema: string | undefined;

	// Das Schema für die API-Zugriffe
	protected _schema_api: ApiSchema | undefined = undefined;

	// Die Api selbst
	protected _api: ApiServer | undefined;

	// Die Api selbst
	protected _apiExternal: ApiExternal | undefined;

	// Die Benutzerdaten des angemeldeten Benutzers
	protected _benutzerdaten = shallowRef<BenutzerDaten | undefined>(undefined);

	// Gibt an, ob der Benutzer Administrator-Rechte hat oder nicht (direkt oder indirekt über eine Gruppen-Zugehörigkeit)
	protected _istAdmin = shallowRef<boolean | undefined>(undefined);

	// Gibt die Kompetenzen des Benutzer zurück, die der Benutzer direkt oder indirekt über eine Gruppen-Zugehörigkeit besitzt
	protected _kompetenzen = shallowRef<Set<BenutzerKompetenz> | undefined>(undefined);

	// Enthält die Klassen-IDs, auf denen der Benutzer aufgrund einer Klassen- oder Abteilungsleitung funktionsbezogene Kompetenzen hat
	protected _kompetenzenKlasse = shallowRef<Set<number> | undefined>(undefined);

	// Enthält die Abiturjahrgänge, bei denen der Benutzer als Beratungslehrer funktionsbezogene Kompetenzen hat
	protected _kompetenzenAbiturjahrgaenge = shallowRef<Set<number> | undefined>(undefined);

	// Die aktuelle Konfiguration der Schule, sofern ein Login stattgefunden hat
	protected _config = ref<Config | undefined>(undefined);

	// Die aktuelle temporäre, nicht in der DB festgehaltene Konfiguration der Schule
	protected _nonPersistentConfig = ref<Config>(new Config(async (_,__) => {}, async (_,__) => { }));

	// Die Stammdaten der Schule, sofern ein Login stattgefunden hat
	protected _stammdaten = shallowRef<{ stammdaten: SchuleStammdaten | undefined, kontext: ValidatorKontext | undefined }>({ stammdaten : undefined, kontext : undefined });

	// Der Modus, in welchem der Server betrieben wird
	protected _serverMode = shallowRef<ServerMode>(ServerMode.STABLE)

	// Die Map mit den CoreTypeDaten
	protected _mapCoreTypeNameJsonData = ref<Map<string, string> | undefined>(undefined);


	/**
	 * Erstellt ein neues Objekt für die Verwaltung der API-Verbindung.
	 */
	public constructor() {
		this._config.value = new Config(this.setConfigGlobal, this.setConfigUser);
	}

	// Gibt die Server-API zurück.
	get api(): ApiServer {
		if (this._api === undefined)
			throw new DeveloperNotificationException("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen")
		return this._api;
	}

	// Gibt die External-API zurück.
	get apiExternal(): ApiExternal {
		if (this._apiExternal === undefined)
			throw new DeveloperNotificationException("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen")
		return this._apiExternal;
	}

	// Gibt das Datenbank-Schema zurück.
	get schema(): string {
		if (this._schema === undefined)
			throw new DeveloperNotificationException("Es liegt kein DB-Schema für die Api vor")
		return this._schema;
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

	// Gibt die Benutzerdaten des angemeldeten Benutzers zurück, sofern ein Login stattgefunden hat
	get benutzerdaten(): BenutzerDaten {
		if (this._benutzerdaten.value === undefined)
			throw new DeveloperNotificationException("Ein Benutzer muss angemeldet sein, damit dessen Daten geladen sein können.");
		return this._benutzerdaten.value;
	}

	// Gibt an, sofern ein Login stattgefunden hat, ob es sich bei dem angemeldeten Benutzer um einen Administrator handelt oder nicht
	get istAdmin(): boolean {
		if (this._istAdmin.value === undefined)
			throw new DeveloperNotificationException("Ein Benutzer muss angemeldet sein, damit ermittelt werden kann, ob es sich um einen Administrator handelt oder nicht.");
		return this._istAdmin.value;
	}

	// Die Kompetenzen des angemeldeten Benutzers, sofern ein Login stattgefunden hat
	get kompetenzen(): Set<BenutzerKompetenz> {
		if (this._kompetenzen.value === undefined)
			throw new DeveloperNotificationException("Ein Benutzer muss angemeldet sein, damit dessen Kompetenzen ermittelt werden können.");
		return this._kompetenzen.value;
	}

	// Die Klassen-IDs, auf denen der angemeldete Benutzer aufgrund einer Klassen- oder Abteilungsleitung funktionsbezogene Kompetenzen hat
	get kompetenzenKlasse(): Set<number> {
		if (this._kompetenzenKlasse.value === undefined)
			throw new DeveloperNotificationException("Ein Benutzer muss angemeldet sein, damit dessen funktionsbezogene Kompetenzen ermittelt werden können.");
		return this._kompetenzenKlasse.value;
	}

	// Die Abiturjahrgänge, auf denen der angemeldete Benutzer als Beratungslehrer funktionsbezogene Kompetenzen hat
	get kompetenzenAbiturjahrgaenge(): Set<number> {
		if (this._kompetenzenAbiturjahrgaenge.value === undefined)
			throw new DeveloperNotificationException("Ein Benutzer muss angemeldet sein, damit dessen funktionsbezogene Kompetenzen ermittelt werden können.");
		return this._kompetenzenAbiturjahrgaenge.value;
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

	// Gibt den Modus zurück, in welchem der Server betrieben wird.
	get mode(): ServerMode {
		return this._serverMode.value;
	}

	// Gibt ein Promise zurück mit einem AES-Schlüssel
	get aes(): AES {
		const aes = this._aes.value;
		if (aes === undefined)
			throw new DeveloperNotificationException("Das AES-Objekt ist nicht definiert")
		return aes;
	}

	// gibt die Map mit den CoreType-Daten zurück
	get mapCoreTypeNameJsonData(): Map<string, string> {
		if (this._mapCoreTypeNameJsonData.value === undefined)
			throw new DeveloperNotificationException("Eine Map mit den CoreType-Daten ist nicht vorhanden.");
		return this._mapCoreTypeNameJsonData.value;
	}

	//** Setzt die Map mit den CoreTypeDaten */
	set mapCoreTypeNameJsonData(map: Map<string, string>) {
		this._mapCoreTypeNameJsonData.value = map;
	}

	/**
	 * Setzt den Benutzer-spezifischen Konfigurationseintrag
	 *
	 * @param key    der Schlüssel des Konfigurationseintrags
	 * @param value  der Wert des Konfigurationseintrags
	 */
	protected setConfigUser = async (key: string, value: string): Promise<void> => {
		await this.api.setClientConfigUserKey(value, this.schema, 'SVWS-Client', key);
	}

	/**
	 * Setzt den globalen Konfigurationseintrag
	 *
	 * @param key    der Schlüssel des Konfigurationseintrags
	 * @param value  der Wert des Konfigurationseintrags
	 */
	protected setConfigGlobal = async (key: string, value: string): Promise<void> => {
		await this.api.setClientConfigGlobalKey(value, this.schema, 'SVWS-Client', key);
	}

	/**
	 * Gibt die Stammdaten der Schule zurück, sofern bereits ein Login stattgefunden hat.
	 *
	 * @returns die Stammdaten
	 */
	get schuleStammdaten(): SchuleStammdaten {
		if (this._stammdaten.value.stammdaten === undefined)
			throw new DeveloperNotificationException("Der Benutzer muss angemeldet sein und die Stammdaten der Schule müssen erfolgreich geladen sein.");
		return this._stammdaten.value.stammdaten;
	}

	/**
	 * Gibt den Validator-Kontext für die Validierung von Statistik-relevanten Daten zurück.
	 *
	 * @returns der Validator-Kontext
	 */
	get validatorKontext(): ValidatorKontext {
		if (this._stammdaten.value.kontext === undefined)
			throw new DeveloperNotificationException("Der Benutzer muss angemeldet sein und der Validator-Kontext muss erfolgreich erstellt sein.");
		return this._stammdaten.value.kontext;
	}

	/**
	 * Stellt eine Verbindung zu dem angebenen Hostnamen her.
	 *
	 * @param hostname   der Hostname, evtl. mit Port-Adresse
	 *
	 * @returns die Liste der Schemata, welche über die Verbindung zur Verfügung stehen.
	 */
	protected async connect(hostname : string): Promise<List<DBSchemaListeEintrag>> {
		const url = `https://${hostname}`;
		const api = new ApiServer(url, "", "");
		const schemata = await api.getConfigDBSchemata();
		this._hostname.value = hostname;
		this._url = url;
		return schemata;
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
	 * Versucht eine Verbindung zu dem SVWS-Server mit dem angegebenen Hostnamen aufzubauen.
	 *
	 * @param {string} name Der Hostname unter der der SVWS-Server erreichbar sein soll
	 *
	 * @returns {Promise<List<DBSchemaListeEintrag>>}
	 */
	connectTo = async (name: string): Promise<List<DBSchemaListeEintrag>> => {
		const url = new URL('https://' + name);
		const host = url.host;
		console.log(`Verbinde zum SVWS-Server unter https://${host}...`);
		try {
			const list = await this.connect(host);
			if (url.port.length > 0)
				localStorage.setItem("SVWS-Client Port", url.port);
			return list;
		} catch (error) {
			console.log(`Verbindung zum SVWS-Server unter https://${host} fehlgeschlagen`);
		}
		const hostname = url.hostname;
		if (host !== hostname) {
			console.log(`Verbinde zum SVWS-Server unter https://${hostname}...`);
			try {
				return await this.connect(hostname)
			} catch (error) {
				console.log(`Verbindung zum SVWS-Server unter https://${hostname} fehlgeschlagen.`);
			}
		}
		const port = localStorage.getItem("SVWS-Client Port");
		if ((port !== null) && (port !== url.port)) {
			console.log(`Verbinde zum SVWS-Server unter https://${hostname}:${port}...`);
			try {
				return await this.connect(`${hostname}:${port}`);
			} catch (error) {
				console.log(`Verbindung zum SVWS-Server unter https://${hostname}:${port} fehlgeschlagen.`);
			}
		}
		throw new UserNotificationException('Es konnte keine Verbindung hergestellt werden.');
	}

	/**
	 * Ermittelt, ob der Benutzer mit den angebenen Daten ein administrativer
	 * Benutzer ist oder nicht.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns true, falls der benutzer Administrator-Rechte hat, und ansonsten false
	 */
	protected getIstAdmin(daten: BenutzerDaten): boolean {
		if (daten.istAdmin)
			return true;
		for (const gruppe of daten.gruppen)
			if (gruppe.istAdmin)
				return true;
		return false;
	}

	/**
	 * Ermittelt, die Menge an Kompetenzen, die der Benutzer mit den angebenen Daten
	 * entweder direkt oder indirekt über eine Gruppe hat.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns die Menge an Kompetenzen
	 */
	protected getKompetenzen(daten: BenutzerDaten): Set<BenutzerKompetenz> {
		const result: Set<BenutzerKompetenz> = new Set();
		// Jeder Benutzer hat die Kompetenz auf Teile Der Applikation zuzugreifen, die keine Kompetenz benötigen
		result.add(BenutzerKompetenz.KEINE);
		// Ein Admin-Benutzer hat alle Kompetenzen...
		const istAdmin = this.getIstAdmin(daten);
		if (istAdmin) {
			result.add(BenutzerKompetenz.ADMIN);
			for (const k of BenutzerKompetenz.values())
				result.add(k);
			return result;
		}
		// Lese die Kompetenzen ein, die der Benutzer direkt hat
		for (const kid of daten.kompetenzen) {
			const k = BenutzerKompetenz.getByID(kid);
			if (k !== null)
				result.add(k);
		}
		// Lese die Kompetenzen ein, die der Benutzer indirekt über eine Gruppe hat
		for (const gruppe of daten.gruppen) {
			for (const kid of gruppe.kompetenzen) {
				const k = BenutzerKompetenz.getByID(kid);
				if (k !== null)
					result.add(k);
			}
		}
		return result;
	}


	/**
	 * Ermittelt, die Menge an Klassen-IDs, auf denen der Benutzer aufgrund einer Klassen- oder Abteilungsleitung
	 * funktionsbezogene Kompetenzen hat.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns die Menge an Klassen-IDs
	 */
	protected getKompetenzenKlasse(daten: BenutzerDaten): Set<number> {
		const result = new Set<number>();
		for (const id of daten.kompetenzenKlassen)
			result.add(id);
		return result;
	}


	/**
	 * Ermittelt, die Menge an Abiturjahrgängen, bei denen der Benutzer als Beratungslehrer
	 * funktionsbezogene Kompetenzen hat.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns die Menge an Abiturjahrgängen
	 */
	protected getKompetenzenAbiturjahrgaenge(daten: BenutzerDaten): Set<number> {
		const result = new Set<number>();
		for (const id of daten.kompetenzenAbiturjahrgaenge)
			result.add(id);
		return result;
	}

	/**
	 * Initialialisiert die Daten, die beim Login geladen werden sollen
	 *
	 * @returns {Promise<boolean>} true beim erfolgreichen Laden der Daten und ansonsten false
	 */
	init = async (): Promise<boolean> => {
		try {
			if ((this._api !== undefined) && (this._schema !== undefined)) {
				const stammdaten = await this._api.getSchuleStammdaten(this._schema);
				const kontext = new ValidatorKontext(stammdaten, false);
				this._stammdaten.value = { stammdaten, kontext };
			}
			return true;
		} catch(error) {
			this._stammdaten.value = { stammdaten: undefined, kontext: undefined };
		}
		return false;
	}


	/**
	 * Liest die Client-Konfiguration vom Server und erstellt das zugehörige
	 * TypeScript-Objekt.
	 *
	 * @returns das Konfigurationsobjekt
	 */
	protected async initConfig(): Promise<void> {
		const cfg = await this.api.getClientConfig(this.schema, 'SVWS-Client');
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
	 * Authentifiziert den angebenen Benutzer mit dem angegebenen Kennwort.
	 *
	 * @param {string} schema   Das Schema
	 * @param {string} username Der Benutzername
	 * @param {string} password Das Kennwort
	 *
	 * @returns {Promise<void>}
	 */
	login = async (schema: string, username: string, password: string): Promise<void> => {
		try {
			if (this._url === undefined)
				throw new DeveloperNotificationException("Keine gültige URL für einen Login verfügbar.");
			this._schema_api = new ApiSchema(this._url, username, password);
			const result = await this._schema_api.revision(schema);
			// TODO verwende revision für Client Check
			console.log(`DB-Revision: ${result}`);
			this._schema = schema;
			this._username = username;
			this._password = password;
			const aesKey = await AES.getKey256(password, username);
			this._aes.value = new AES(AESAlgo.CBC, aesKey);
			this._api = new ApiServer(this._url, this._username, this._password);
			this._apiExternal = new ApiExternal(this._url, this._username, this._password);
			this._authenticated.value = true;
			this._benutzerdaten.value = await this._api.getBenutzerDatenEigene(this._schema);
			this._istAdmin.value = this.getIstAdmin(this._benutzerdaten.value);
			this._kompetenzen.value = this.getKompetenzen(this._benutzerdaten.value);
			this._kompetenzenKlasse.value = this.getKompetenzenKlasse(this._benutzerdaten.value);
			this._kompetenzenAbiturjahrgaenge.value = this.getKompetenzenAbiturjahrgaenge(this._benutzerdaten.value);
			this._serverMode.value = ServerMode.getByText(await this._api.getServerModus());
			await this.initConfig();
		} catch (error) {
			// Wenn Status 404, dann ist das Schema noch nicht initialisiert
			if ((error instanceof OpenApiError) && (error.response?.status === 404))
				return;
			if ((error instanceof OpenApiError) && (error.response?.status === 503)) {
				const res = await error.response.text();
				throw new UserNotificationException(res);
			}
			// TODO Anmelde-Fehler wird nur in der App angezeigt. Der konkreten Fehler könnte ggf. geloggt werden...
			this._authenticated.value = false;
			this._benutzerdaten.value = undefined;
			this._istAdmin.value = undefined;
			this._kompetenzen.value = undefined;
			this._kompetenzenKlasse.value = undefined;
			this._kompetenzenAbiturjahrgaenge.value = undefined;
			this.config.mapGlobal = new Map();
			this.config.mapUser = new Map();
			this.nonPersistentConfig.mapGlobal = new Map();
			this.nonPersistentConfig.mapUser = new Map();
			this._stammdaten.value = { stammdaten: undefined, kontext: undefined };
			this._serverMode.value = ServerMode.STABLE;
			this._aes.value = undefined;
		}
	}


	/**
	 * Trennt die Verbindung für den aktuell angemeldeten Benutzer
	 */
	logout = async (): Promise<void> => {
		this._authenticated.value = false;
		this._stammdaten.value = { stammdaten: undefined, kontext: undefined };
		this._benutzerdaten.value = undefined;
		this._istAdmin.value = undefined;
		this._kompetenzen.value = undefined;
		this._kompetenzenKlasse.value = undefined;
		this._kompetenzenAbiturjahrgaenge.value = undefined;
		this._username = "";
		this._password = "";
		this._schema_api = undefined;
		this._aes.value = undefined;
		this._api = undefined;
		this._apiExternal = undefined;
		this.config.mapGlobal = new Map();
		this.config.mapUser = new Map();
		this.nonPersistentConfig.mapGlobal = new Map();
		this.nonPersistentConfig.mapUser = new Map();
	}

	/**
	 * Informiert die Api-Verbindung, dass ihre Daten, z.B. die Stammdaten der Schule angepasst wurden
	 */
	updatedApiData = () => {
		this._stammdaten.value = { ... this._stammdaten.value };
	}

}

