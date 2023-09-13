import type { BenutzerDaten, DBSchemaListeEintrag, List, SchuleStammdaten} from '@core';
import { ApiSchema, ApiServer, BenutzerKompetenz, ArrayList } from '@core';


export class ApiConnection {

	// Gibt an, ob der Client beim Server authentifiziert ist
	protected _authenticated: boolean = false;

	// Der Hostname (evtl. mit Port) des Servers, bei dem der Login stattfindet
	protected _hostname: string = 'localhost'

	// Die URL mit welcher der Server verbunden ist
	protected _url: string | undefined = undefined;

	// Der Benutzername für den Login
	protected _username = "";

	// Das Kennwort für den Login
	protected _password = "";

	// Der Name des Schemas auf dem SVWS-Server, bei dem der Login stattfindet
	protected _schema: string | undefined;

	// Das Schema für die API-Zugriffe
	protected _schema_api: ApiSchema | undefined = undefined;

	// Die Api selbst
	protected _api: ApiServer | undefined;

	// Die Benutzerdaten des angemeldeten Benutzers
	protected _benutzerdaten: BenutzerDaten | undefined = undefined;

	// Gibt an, ob der Benutzer Administrator-Rechte hat oder nicht (direkt oder indirekt über eine Gruppen-Zugehörigkeit)
	protected _istAdmin: boolean | undefined = undefined;

	// Gibt die Kompetenzen des Benutzer zurück, die der Benutzer direkt oder indirekt über eine Gruppen-Zugehörigkeit besitzt
	protected _kompetenzen: Set<BenutzerKompetenz> | undefined = undefined;



	// Die Stammdaten der Schule, sofern ein Login stattgefunden hat
	protected _stammdaten: SchuleStammdaten | undefined = undefined;



	// Gibt die Server-API zurück.
	get api(): ApiServer {
		if (this._api === undefined)
			throw new Error("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen")
		return this._api;
	}

	// Gibt das Datenbank-Schema zurück.
	get schema(): string {
		if (this._schema === undefined)
			throw new Error("Es liegt kein DB-Schema für die Api vor")
		return this._schema;
	}

	// Gibt den Hostname zurück
	get hostname() : string {
		return this._hostname;
	}

	// Gibt den Status zurück, ob der Benutzer authentifiziert wurde
	get authenticated() : boolean {
		return this._authenticated;
	}

	// Gibt den Benutzernamen zurück
	get username() : string {
		return this._username;
	}

	// Gibt die Benutzerdaten des angemeldeten Benutzers zurück, sofern ein Login stattgefunden hat
	get benutzerdaten(): BenutzerDaten {
		if (this._benutzerdaten === undefined)
			throw new Error("Ein Benutzer muss angemeldet sein, damit dessen Daten geladen sein können.");
		return this._benutzerdaten;
	}

	// Gibt an, sofern ein Login stattgefunden hat, ob es sich bei dem angemeldeten Benutzer um einen Administrator handelt oder nicht
	get istAdmin(): boolean {
		if (this._istAdmin === undefined)
			throw new Error("Ein Benutzer muss angemeldet sein, damit ermittelt werden kann, ob es sich um einen Administrator handelt oder nicht.");
		return this._istAdmin;
	}

	// Die Kompetenzen des angemeldeten Benutzers, sofern ein Login stattgefunden hat
	get kompetenzen(): Set<BenutzerKompetenz> {
		if (this._kompetenzen === undefined)
			throw new Error("Ein Benutzer muss angemeldet sein, damit dessen Kompetenzen ermittelt werden können.");
		return this._kompetenzen;
	}

	// Gibt die Stammdaten der Schule zurück, sofern ein Login sattgefunden hat
	get schuleStammdaten(): SchuleStammdaten {
		if (this._stammdaten === undefined)
			throw new Error("Der Benutzer muss angemeldet sein und die Stammdaten der Schule müssen erfolgreich geladen sein.");
		return this._stammdaten;
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
		this._hostname = hostname;
		this._url = url;
		return schemata;
	}

	/**
	 * Setzt den Hostnamen, der für die Verbindung verwendet wird.
	 *
	 * @param hostname    der Hostname
	 */
	setHostname = (hostname: string): void => {
		this._hostname = hostname;
	}

	/**
	 * Versucht eine Verbindung zu dem SVWS-Server mit dem angegebenen Hostnamen aufzubauen.
	 *
	 * @param {string} hostname Der Hostname unter der der SVWS-Server erreichbar sein soll
	 *
	 * @returns {Promise<List<DBSchemaListeEintrag>>}
	 */
	connectTo = async (name: string): Promise<List<DBSchemaListeEintrag>> => {
		const url = new URL('https://' + name);
		const host = url.host;
		console.log(`Verbinde zum SVWS-Server unter https://${host}...`);
		try {
			return await this.connect(host);
		} catch (error) {
			console.log(`Verbindung zum SVWS-Server unter https://${host} fehlgeschlagen`, error);
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
		return new ArrayList<DBSchemaListeEintrag>();
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
				throw new Error("Keine gültige URL für einen Login verfügbar.");
			this._schema_api = new ApiSchema(this._url, username, password);
			const result = await this._schema_api.revision(schema);
			// TODO verwende revision für Client Check
			console.log(`DB-Revision: ${result}`);
			this._schema = schema;
			this._username = username;
			this._password = password;
			this._api = new ApiServer(this._url, this._username, this._password);
			this._authenticated = true;
			this._benutzerdaten = await this._api.getBenutzerDatenEigene(this._schema);
			this._istAdmin = this.getIstAdmin(this._benutzerdaten);
			this._kompetenzen = this.getKompetenzen(this._benutzerdaten);

		} catch (error) {
			// TODO Anmelde-Fehler wird nur in der App angezeigt. Der konkreten Fehler könnte ggf. geloggt werden...
			this._authenticated = false;
			this._benutzerdaten = undefined;
			this._istAdmin = undefined;
			this._kompetenzen = undefined;
			this._stammdaten = undefined;
		}
	}

	/**
	 * Initialialisiert die Daten, die beim Login geladen werden sollen
	 *
	 * @returns {Promise<boolean>} true beim erfolgreichen Laden der Daten und ansonsten false
	 */
	init = async (): Promise<boolean> => {
		try {
			if (this._api && this._schema)
				this._stammdaten = await this._api.getSchuleStammdaten(this._schema);
			return true;
		} catch(error) {
			this._stammdaten = undefined;
		}
		return false;
	}

	/**
	 * Trennt die Verbindung für den aktuell angemeldeten Benutzer
	 */
	logout = async (): Promise<void> => {
		this._authenticated = false;
		this._stammdaten = undefined;
		this._benutzerdaten = undefined;
		this._istAdmin = undefined;
		this._kompetenzen = undefined;
		this._username = "";
		this._password = "";
		this._schema_api = undefined;
	}

}

