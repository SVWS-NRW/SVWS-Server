import { ApiSchema, ApiServer, DBSchemaListeEintrag, List, SchuleStammdaten, Vector } from "@svws-nrw/svws-core-ts";
import { Ref, ref, ShallowRef, shallowRef } from "vue";

export class ApiConnection {

	// Gibt an, ob der Client beim Server authentifiziert ist
	protected _authenticated: Ref<boolean> = ref(false);

	// Der Hostname (evtl. mit Port) des Servers, bei dem der Login stattfindet
	protected _hostname: Ref<string> = ref(window.location.hostname + ":" + window.location.port);

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

	// TODO Benutzerkomptenzen des angemeldeten Benutzers

	// Die Stammdaten der Schule, sofern ein Login stattgefunden hat
	protected _stammdaten: ShallowRef<SchuleStammdaten | undefined> = shallowRef(undefined);


	get api(): ApiServer {
		if (this._api === undefined)
			throw new Error("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen")
		return this._api;
	}

	get schema(): string {
		if (this._schema === undefined)
			throw new Error("Es liegt kein DB-Schema für die Api vor")
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

	// Gibt die Stammdaten der Schule zurück, sofern ein Login sattgefunden hat
	get schuleStammdaten(): SchuleStammdaten {
		if (this._stammdaten.value === undefined)
			throw new Error("Der Benutzer muss angemeldet sein und die Stammdaten der Schule müssen erfolgreich geladen sein.");
		return this._stammdaten.value;
	}

	private async connect(hostname : string): Promise<List<DBSchemaListeEintrag>> {
		const url = `https://${hostname}`;
		const api = new ApiServer(url, "", "");
		const schemata = await api.getConfigDBSchemata();
		this._hostname.value = hostname;
		this._url = url;
		return schemata;
	}

	setHostname = (hostname: string): void => {
		this._hostname.value = hostname;
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
			console.log(`Verbindung zum SVWS-Server unter https://${host}`);
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
		return new Vector<DBSchemaListeEintrag>();
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
			this._authenticated.value = true;
			this._stammdaten.value = await this._api.getSchuleStammdaten(this._schema);
		} catch (error) {
			// TODO Anmelde-Fehler wird nur in der App angezeigt. Der konkreten Fehler könnte ggf. geloggt werden...
			this._authenticated.value = false;
		}
	}

	logout = async (): Promise<void> => {
		this._authenticated.value = false;
		this._stammdaten.value = undefined;
		this._username = "";
		this._password = "";
		this._schema_api = undefined;
	}

}

