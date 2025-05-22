import { ref } from "vue";

import type { DBSchemaListeEintrag } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";
import { routeInit } from "~/router/init/RouteInit";

import SLogin from "~/components/SLogin.vue";
import type { LoginProps } from "~/components/SLoginProps";
import type { RouteParams, RouteLocationRaw } from "vue-router";

export class RouteLogin extends RouteNode<any, any> {

	protected defaultChildNode = undefined;

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";
	protected schema = ref<string | null>(null);

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "login", "/login/:schema?", SLogin);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Login";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { schema } = RouteNode.getStringParams(to_params, ["schema"]);
			this.schema.value = schema ?? null;
		} catch (e) {
			console.log('Es wurde ein falscher Schema-Parameter übergeben, Login trotzdem fortsetzen: ', e)
		}
	}

	public login = async (schema: string, username: string, password: string): Promise<void> => {
		await api.login(schema, username, password);
		if (api.authenticated) {
			if (await api.init()) {
				if (this.routepath !== "/")
					// Überprüfe das Schema, falls ein redirect nach dem Login geplant ist
					if (!this.routepath.startsWith("/" + encodeURIComponent(schema)))
						this.routepath = "/";
				await RouteManager.doRoute(this.routepath);
				return;
			}
			if (api.benutzerIstAdmin)
				await RouteManager.doRoute(routeInit.name);
		}
	}

	public logout = async () => {
		this.routepath = "/";
		this.schema.value = api.schema;
		await RouteManager.doRoute(this.getRoute());
		await api.logout();
		RouteManager.resetRouteState();
	}

	public setSchema = async (schema: DBSchemaListeEintrag) => {
		this.schema.value = schema.name;
	}

	public getProps(): LoginProps {
		return {
			setHostname: api.setHostname,
			setSchema: this.setSchema,
			login: this.login,
			connectTo: api.connectTo,
			authenticated: api.authenticated,
			hostname: api.hostname,
			schemaPrevious: this.schema.value,
			servermode: api.mode,
			setMapCoreTypeNameJsonData: api.setMapCoreTypeNameJsonData,
		}
	}

}

export const routeLogin = new RouteLogin();
