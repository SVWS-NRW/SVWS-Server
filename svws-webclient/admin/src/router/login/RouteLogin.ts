import { ref } from "vue";
import type { RouteLocationRaw } from "vue-router";
import { ServerMode } from "@core/core/types/ServerMode";
import type { LoginProps } from "@admin/components/SLoginProps";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { RouteNode } from "../RouteNode";

const SLogin = () => import("@admin/components/SLogin.vue");

export class RouteLogin extends RouteNode<unknown, any> {

	protected defaultChildNode = undefined;

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";
	public redirect = '';
	protected schema = ref<string | null>(null);

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
		if (api.authenticated) {
			await RouteManager.doRoute(this.routepath);
		}
	};

	public logout = async () => {
		this.routepath = "/";
		await RouteManager.doRoute({ name: this.name });
		await api.logout();
	};

	public getProps(): LoginProps {
		return {
			login: this.login,
			connectTo: api.connectTo,
			authenticated: api.authenticated,
			schemaPrevious: this.schema.value,
		};
	}

}

export const routeLogin = new RouteLogin();
