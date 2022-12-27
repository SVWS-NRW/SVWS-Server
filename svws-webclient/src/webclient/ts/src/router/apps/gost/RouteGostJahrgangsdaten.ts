import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost } from "~/router/apps/RouteGost";

const SGostStammdaten = () => import("~/components/gost/stammdaten/SGostStammdaten.vue");

export class RouteGostJahrgangsdaten extends RouteNode<unknown> {

	public constructor() {
		super("gost_jahrgangsdaten", "daten", SGostStammdaten);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Allgemein";
	}

}

export const routeGostJahrgangsdaten = new RouteGostJahrgangsdaten();
