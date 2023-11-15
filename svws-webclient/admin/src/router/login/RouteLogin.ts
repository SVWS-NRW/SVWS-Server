import type { Ref} from "vue";
import { ref } from "vue";
import type { RouteLocationRaw } from "vue-router";

import { ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";

import SLogin from "~/components/SLogin.vue";
import type { LoginProps } from "~/components/SLoginProps";

export class RouteLogin extends RouteNode<unknown, any> {

	protected defaultChildNode = undefined;

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";
	public redirect = '';
	protected schema: Ref<string | null> = ref(null);

	public constructor() {
		super("login", "/login/:schemaname?", SLogin);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Login";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public login = async (username: string, password: string): Promise<void> => {
		await api.login(username, password);
		if (api.authenticated)
			await RouteManager.doRoute(this.routepath);
	}

	public logout = async () => {
		this.routepath = "/";
		await RouteManager.doRoute({ name: this.name });
		await api.logout();
	}

	public getProps(): LoginProps {
		return {
			setHostname: api.setHostname,
			login: this.login,
			connectTo: api.connectTo,
			authenticated: api.authenticated,
			hostname: api.hostname,
			schemaPrevious: this.schema.value,
		}
	}

}

export const routeLogin = new RouteLogin();
