import SLogin from "~/components/SLogin.vue";
import { RouteNode } from "~/router/RouteNode";


export class RouteLogin extends RouteNode<unknown, any> {

	protected defaultChildNode = undefined;

	public constructor() {
		super("login", "/login", SLogin);
		super.text = "Login";
	}

}

export const routeLogin = new RouteLogin();
