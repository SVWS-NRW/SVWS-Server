import { ref, shallowRef } from "vue";

import { ApiSchemaPrivileged, BenutzerKennwort, UserNotificationException, OpenApiError, ApiServer, ServerMode } from "@core";

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

	// Die API für die priviligierten Schema-Zugriffe
	protected _schema_api_privileged: ApiSchemaPrivileged | undefined = undefined;

	// Die Api selbst
	protected _api: ApiServer | undefined;

	// Der Modus, in welchem der Server betrieben wird
	protected _serverMode = shallowRef<ServerMode>(ServerMode.STABLE)

	// Gibt an, ob es sich um einen Benutzer mit Rechten auf root hat oder nicht
	protected _hasRootPrivileges: boolean = false;


	// Gibt die Server-API zurück.
	get api(): ApiServer {
		if (this._api === undefined)
			throw new Error("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen")
		return this._api;
	}

	// Gibt die API für den priviligierten Schema-Zugriff zurück.
	get api_privileged(): ApiSchemaPrivileged {
		if (this._schema_api_privileged === undefined)
			throw new Error("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können für den priviligierten Schema-Zugriff nicht erfolgen")
		return this._schema_api_privileged;
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

	// Gibt zurück, ob der angemeldete Benutzer root-Privilegien besitzt oder nicht
	get hasRootPrivileges() : boolean {
		return this._hasRootPrivileges;
	}

	// Gibt den Modus zurück, in welchem der Server betrieben wird.
	get mode(): ServerMode {
		return this._serverMode.value;
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
	 * @param {string} hostname Der Hostname unter der der SVWS-Server erreichbar sein soll
	 *
	 * @returns {Promise<boolean>}
	 */
	connectTo = async (name: string): Promise<boolean> => {
		let urlString = `https://${name}`;
		let url = new URL(urlString);
		const host = url.host;
		console.log(`Verbinde zum SVWS-Server unter https://${host}...`);
		try {
			const api = new ApiSchemaPrivileged(urlString, "", "");
			await api.getSchemaListe();
			return true;
		} catch (error) {
			if ((error instanceof OpenApiError) && error.response?.status === 401) {
				this._hostname.value = host;
				this._url = urlString;
				return true;
			}
			console.log(`Verbindung zum SVWS-Server unter https://${host} fehlgeschlagen`);
		}
		const hostname = url.hostname;
		urlString = `https://${hostname}`;
		url = new URL(urlString);
		if (host !== hostname) {
			console.log(`Verbinde zum SVWS-Server unter https://${hostname}...`);
			try {
				const api = new ApiSchemaPrivileged(urlString, "", "");
				await api.getSchemaListe();
				return true;
			} catch (error) {
				if ((error instanceof OpenApiError) && error.response?.status === 401) {
					this._hostname.value = hostname;
					this._url = urlString;
					return true;
				}
				console.log(`Verbindung zum SVWS-Server unter https://${hostname} fehlgeschlagen.`);
			}
		}
		return false;
	}


	/**
	 * Authentifiziert den angebenen Benutzer mit dem angegebenen Kennwort.
	 *
	 * @param {string} username Der Benutzername
	 * @param {string} password Das Kennwort
	 *
	 * @returns {Promise<boolean>}
	 */
	login = async (username: string, password: string): Promise<boolean> => {
		try {
			if (this._url === undefined)
				throw new Error("Keine gültige URL für einen Login verfügbar.");
			const api_priv = new ApiSchemaPrivileged(this._url, username, password);
			const data = new BenutzerKennwort();
			data.user = username;
			data.password = password;
			await api_priv.getSchemaListe();
			try {
				const isPrivilegedRoot = await api_priv.checkDBPassword(data);
				if ((isPrivilegedRoot === null) || (isPrivilegedRoot === false))
					throw new UserNotificationException("Der Datenbank-Benutzer besitzt nicht die nötigen Privilegien.");
				this._hasRootPrivileges = true;
			} catch (error) {
				this._hasRootPrivileges = false;
			}
			this._schema_api_privileged = api_priv;
			this._username = username;
			this._password = password;
			this._authenticated.value = true;
			this._api = new ApiServer(this._url, "", "");
			this._serverMode.value = ServerMode.getByText(await this._api.getServerModus());
			return true;
		} catch (error) {
			// TODO Anmelde-Fehler wird nur in der App angezeigt. Der konkreten Fehler könnte ggf. geloggt werden...
			this._authenticated.value = false;
			this._serverMode.value = ServerMode.STABLE;
			this._api = undefined;
			this._schema_api_privileged = undefined;
			return false;
		}
	}

	/**
	 * Trennt die Verbindung für den aktuell angemeldeten Benutzer
	 */
	logout = async (): Promise<void> => {
		this._authenticated.value = false;
		this._username = "";
		this._password = "";
		this._api = undefined;
		this._schema_api_privileged = undefined;
	}

}

