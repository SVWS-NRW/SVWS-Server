import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost } from "~/router/apps/RouteGost";

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausurplanung/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<unknown> {

	public constructor() {
		super("gost_klausurplanung_schienen", "schienen", SGostKlausurplanungSchienen);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Schienen";
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();


