import type { RouteLocationRaw } from "vue-router";
import type { InitProps } from "~/components/init/SInitProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "./RouteManager";
import { routeInitSchulkatalog } from "./init/RouteInitSchulkatalog";
import { routeInitSchild2 } from "./init/RouteInitSchild2";
import { routeInitBackup } from "./init/RouteInitBackup";

const SInitWrapper = () => import("~/components/init/SInitWrapper.vue")

export class RouteInit extends RouteNode<unknown, any> {

	protected mapRoutes: Map<string, RouteNode<unknown, any>> = new Map();

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "init", "/init", SInitWrapper);
		super.propHandler = (route) => this.getProps();
		super.text = "Initialisierung";
		super.children = [
			routeInitSchulkatalog,
			routeInitSchild2,
			routeInitBackup,
		]
		this.mapRoutes.set('schulkatalog', routeInitSchulkatalog);
		this.mapRoutes.set('backup', routeInitBackup);
		this.mapRoutes.set('schild2', routeInitSchild2);
	}

	goto = async (route: string) => {
		const name = this.mapRoutes.get(route)?.name;
		if (name)
			await RouteManager.doRoute({name});
	};

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public getProps(): InitProps {
		return {
			goto: this.goto,
		}
	}

}

export const routeInit = new RouteInit();
