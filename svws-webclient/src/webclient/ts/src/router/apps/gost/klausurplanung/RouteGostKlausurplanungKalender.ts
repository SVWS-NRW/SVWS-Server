import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost } from "~/router/apps/RouteGost";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<unknown> {

	public constructor() {
		super("gost_klausurplanung_kalender", "kalender", SGostKlausurplanungKalender);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Kalender";
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

