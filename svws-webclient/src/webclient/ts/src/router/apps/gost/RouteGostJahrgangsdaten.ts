import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";

const SGostStammdaten = () => import("~/components/gost/stammdaten/SGostStammdaten.vue");

export class RouteGostJahrgangsdaten extends RouteNode<unknown> {

	public constructor() {
		super("gost_jahrgangsdaten", "daten", SGostStammdaten);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Allgemein";
	}

}

export const routeGostJahrgangsdaten = new RouteGostJahrgangsdaten();
