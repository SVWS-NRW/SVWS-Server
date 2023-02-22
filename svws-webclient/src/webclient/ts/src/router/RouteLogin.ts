import { RouteLocationRaw } from "vue-router";
import SLogin from "~/components/SLogin.vue";
import { RouteNode } from "~/router/RouteNode";
import { api } from "./Api";
import { RouteManager } from "./RouteManager";

export class RouteLogin extends RouteNode<unknown, any> {

	protected defaultChildNode = undefined;

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";

	public constructor() {
		super("login", "/login", SLogin);
		super.propHandler = (route) => this.getProps();
		super.text = "Login";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public login = async (schema: string, username: string, password: string): Promise<void> => {
		await api.login(schema, username, password);
		if (api.authenticated)
			await RouteManager.doRoute(this.routepath);
	}

	public logout = async () => {
		await api.logout();
		this.routepath = "/";
		await RouteManager.doRoute({ name: this.name });
	}

	public getProps() {
		return {
			setHostname: api.setHostname,
			login: this.login,
			connectTo: api.connectTo,
			authenticated: api.authenticated,
			hostname: api.hostname
		}
	}

}

export const routeLogin = new RouteLogin();
