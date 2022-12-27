import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost } from "~/router/apps/RouteGost";

const SGostKlausurplanungDaten = () => import("~/components/gost/klausurplanung/SGostKlausurplanungDaten.vue");

export class RouteGostKlausurplanungKlausurdaten extends RouteNode<unknown> {

	public constructor() {
		super("gost_klausurplanung_klausurdaten", "klausurdaten", SGostKlausurplanungDaten);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Klausurdaten";
	}

}

export const routeGostKlausurplanungKlausurdaten = new RouteGostKlausurplanungKlausurdaten();

