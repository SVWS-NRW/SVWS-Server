import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";
import SLogin from "~/components/SLogin.vue";
import type { LoginProps } from "~/components/SLoginProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

export class RouteLogin extends RouteNode<any, any> {

	protected defaultChildNode = undefined;

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "login", "/login", SLogin);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Login";
	}

	public login = async (username: string, password: string): Promise<void> => {
		let success = await api.login(username, password);
		if (success) {
			success = await api.init();
			if (success)
				await RouteManager.doRoute(this.routepath);
		}
	}

	public logout = async () => {
		this.routepath = "/";
		await RouteManager.doRoute(this.getRoute());
		await api.logout();
		RouteManager.resetRouteState();
	}

	public getProps(): LoginProps {
		return {
			setHostname: api.setHostname,
			login: this.login,
			connectTo: api.connectTo,
			authenticated: api.authenticated,
			hostname: api.hostname,
		}
	}

}

export const routeLogin = new RouteLogin();
