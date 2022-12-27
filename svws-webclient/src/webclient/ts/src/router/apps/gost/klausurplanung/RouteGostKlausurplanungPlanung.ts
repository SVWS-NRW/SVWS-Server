import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost } from "~/router/apps/RouteGost";

const SGostKlausurplanungPlanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanungPlanung.vue");

export class RouteGostKlausurplanungPlanung extends RouteNode<unknown> {

	public constructor() {
		super("gost_klausurplanung_planung", "planung", SGostKlausurplanungPlanung);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Detailplanung";
	}

}

export const routeGostKlausurplanungPlanung = new RouteGostKlausurplanungPlanung();

