import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

const SGostStammdaten = () => import("~/components/gost/stammdaten/SGostStammdaten.vue");

export class RouteGostJahrgangsdaten extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super("gost_jahrgangsdaten", "daten", SGostStammdaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemein";
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: routeGost.data.jahrgangsdaten.daten,
		};
	}

}

export const routeGostJahrgangsdaten = new RouteGostJahrgangsdaten();
