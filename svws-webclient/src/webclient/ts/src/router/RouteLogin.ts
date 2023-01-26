import { RouteLocationRaw } from "vue-router";
import SLogin from "~/components/SLogin.vue";
import { RouteNode } from "~/router/RouteNode";


export class RouteLogin extends RouteNode<unknown, any> {

	protected defaultChildNode = undefined;

	public constructor() {
		super("login", "/login", SLogin);
		super.text = "Login";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

}

export const routeLogin = new RouteLogin();
