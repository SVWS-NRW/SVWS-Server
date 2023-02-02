import { ApiSchema, ApiServer, DBSchemaListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationRaw } from "vue-router";
import { App } from "~/apps/BaseApp";
import SLogin from "~/components/SLogin.vue";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "./RouteApp";
import { RouteManager } from "./RouteManager";

export class RouteDataLogin {

	// Gibt an, ob der Client beim Server authentifiziert ist
	protected _authenticated = false;

	// Der Hostname (evtl. mit Port) des Servers, bei dem der Login stattfindet
	protected _hostname = "localhost";

	// Der Benutzername für den Login
	protected _username = "";

	// Das Kennwort für den Login
	protected _password = "";

	// Der Name des Schemas auf dem SVWS-Server, bei dem der Login stattfindet
	protected _schema: string | undefined;

	// Das Schema für die API-Zugriffe
	protected _schema_api: ApiSchema | undefined = undefined;

	// Gibt den Status zurück, ob der Benutzer authentifiziert wurde
	get authenticated() : boolean {
		return this._authenticated;
	}

	// Gibt den Benutzernamen zurück
	get username() : string {
		return this._username;
	}

	/**
	 * Versucht eine Verbindung zu dem SVWS-Server unter der angegebenen URL aufzubauen.
	 *
	 * @param {string} url Die URL unter der der SVWS-Server erreichbar sein soll
	 * @returns {Promise<void>}
	 */
	connectTo: (url: string) => Promise<List<DBSchemaListeEintrag>> = async (url: string) => {
		const api_url = new ApiServer(`https://${url}`, "", "");
		let schemata: List<DBSchemaListeEintrag> = new Vector();
		try {
			schemata = await api_url.getConfigDBSchemata();
			this._hostname = `https://${url}`;
		} catch (error) {
			try {
				console.log(`Verbindung zum SVWS-Server unter https://${url} fehlgeschlagen.`);
				const api_localhost = new ApiServer(`https://localhost`, "", "");
				schemata = await api_localhost.getConfigDBSchemata();
				this._hostname = `https://localhost`;
			} catch (error) {
				console.log(`Verbindung zum SVWS-Server unter https://localhost fehlgeschlagen.`);
			}
		}
		return schemata;
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
			this._schema_api = new ApiSchema(this._hostname, username, password);
			const result = await this._schema_api.revision(schema);
			// TODO verwende revision für Client Check
			console.log(`DB-Revision: ${result}`);
			this._schema = schema;
			this._username = username;
			this._password = password;
			App.setup({
				url: this._hostname,
				username: this._username,
				password: this._password,
				schema: this._schema
			});
			this._authenticated = true;
			await RouteManager.doRoute({ name: routeApp.name });
		} catch (error) {
			// TODO error z.B. loggen
			console.log(error)
			this._authenticated = false;
		}
	}

	public async logout() {
		this._authenticated = false;
		this._username = "";
		this._password = "";
		this._schema_api = undefined;
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
			login: this.data.login,
			connectTo: this.data.connectTo,
			authenticated: this.data.authenticated
		}
	}

}

export const routeLogin = new RouteLogin();
