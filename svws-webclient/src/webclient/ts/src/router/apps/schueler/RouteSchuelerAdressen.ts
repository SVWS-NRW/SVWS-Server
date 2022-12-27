import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SSchuelerAdressen = () => import("~/components/schueler/adressen/SSchuelerAdressen.vue");

export class RouteSchuelerAdressen extends RouteNode<unknown> {

	public constructor() {
		super("schueler_adressen", "adressen", SSchuelerAdressen);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl);
		super.text = "Adressen / Betriebe";
	}

}

export const routeSchuelerAdressen = new RouteSchuelerAdressen();

