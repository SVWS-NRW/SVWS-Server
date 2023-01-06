import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";

const SGostStammdaten = () => import("~/components/gost/stammdaten/SGostStammdaten.vue");

export class RouteGostJahrgangsdaten extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super("gost_jahrgangsdaten", "daten", SGostStammdaten);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Allgemein";
	}

}

export const routeGostJahrgangsdaten = new RouteGostJahrgangsdaten();
