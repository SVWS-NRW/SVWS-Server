import { ApiSchema, ApiServer, DBSchemaListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { ref, Ref } from "vue";
import { RouteLocationRaw } from "vue-router";
import { App } from "~/apps/BaseApp";
import SLogin from "~/components/SLogin.vue";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "./RouteManager";

export class RouteDataLogin {

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";

	// Gibt an, ob der Client beim Server authentifiziert ist
	protected _authenticated: Ref<boolean> = ref(false);

	// Der Hostname (evtl. mit Port) des Servers, bei dem der Login stattfindet
	protected _hostname: Ref<string> = ref("localhost");

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

	private async connect(hostname : string): Promise<List<DBSchemaListeEintrag>> {
		const url = `https://${hostname}`;
		const api = new ApiServer(url, "", "");
		const schemata = await api.getConfigDBSchemata();
		this._hostname.value = hostname;
		this._url = url;
		return schemata;
	}

	setHostname = (hostname: string) => {
		this._hostname.value = hostname;
	}

	/**
	 * Versucht eine Verbindung zu dem SVWS-Server mit dem angegebenen Hostnamen aufzubauen.
	 *
	 * @param {string} hostname Der Hostname unter der der SVWS-Server erreichbar sein soll
	 *
	 * @returns {Promise<DBSchemaListeEintrag>}
	 */
	connectTo: (hostname: string) => Promise<List<DBSchemaListeEintrag>> = async (hostname: string) => {
		console.log(`Verbinde zum SVWS-Server unter https://${hostname}...`);
		try {
			return await this.connect(hostname);
		} catch (error) {
			console.log(`Verbindung zum SVWS-Server unter https://${hostname} fehlgeschlagen.`);
		}
		const hostname2 = hostname.split(":")[0];
		if (hostname2 !== hostname) {
			console.log(`Verbinde zum SVWS-Server unter https://${hostname2}...`);
			try {
				return await this.connect(hostname2)
			} catch (error) {
				console.log(`Verbindung zum SVWS-Server unter https://${hostname2} fehlgeschlagen.`);
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
	login: (schema: string, username: string, password: string) => Promise<void> = async (schema: string, username: string, password: string) => {
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
			App.setup({
				url: this._url,
				username: this._username,
				password: this._password,
				schema: this._schema
			});
			this._authenticated.value = true;
			await RouteManager.doRoute(this.routepath);
		} catch (error) {
			// TODO Anmelde-Fehler wird nur in der App angezeigt. Der konkreten Fehler könnte ggf. geloggt werden...
			this._authenticated.value = false;
		}
	}

	public async logout() {
		this._authenticated.value = false;
		this._username = "";
		this._password = "";
		this._schema_api = undefined;
		this.routepath = "/";
		// TODO BaseApp Api-Server entfernen
		await RouteManager.doRoute({ name: routeLogin.name });
	}

}

export class RouteLogin extends RouteNode<RouteDataLogin, any> {

	protected defaultChildNode = undefined;

	public constructor() {
		super("login", "/login", SLogin, new RouteDataLogin());
		super.propHandler = (route) => this.getProps();
		super.text = "Login";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public getProps() {
		return {
			setHostname: this.data.setHostname,
			login: this.data.login,
			connectTo: this.data.connectTo,
			authenticated: this.data.authenticated,
			hostname: this.data.hostname
		}
	}

}

export const routeLogin = new RouteLogin();
