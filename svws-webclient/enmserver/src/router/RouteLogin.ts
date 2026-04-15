import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import SLogin from "~/components/SLogin.vue";
import type { LoginProps } from "~/components/SLoginProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { authState } from "~/states/AuthStateImpl";

export class RouteLogin extends RouteNode<any, any> {

	protected defaultChildNode = undefined;

	// Der Pfad, zu welchem weitergeleitet wird
	public routepath = "/";

	public constructor() {
		super(Schulform.values(), "login", "/login", SLogin);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Login";
	}

	public finishLogin = async (): Promise<void> => {
		await RouteManager.doRoute(this.routepath);
	};

	public logout = async () => {
		this.routepath = "/";
		await authState.logout();
		await RouteManager.doRoute(this.getRoute());
		RouteManager.resetRouteState();
	};

	public getProps(): LoginProps {
		return {
			finishLogin: this.finishLogin,
		};
	}

}

export const routeLogin = new RouteLogin();
