import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost } from "~/router/apps/RouteGost";

const SGostKlausurplanungKonflikte = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKonflikte.vue");

export class RouteGostKlausurplanungKonflikte extends RouteNode<unknown> {

	public constructor() {
		super("gost_klausurplanung_konflikte", "konflikte", SGostKlausurplanungKonflikte);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Konflikte";
	}

}

export const routeGostKlausurplanungKonflikte = new RouteGostKlausurplanungKonflikte();
