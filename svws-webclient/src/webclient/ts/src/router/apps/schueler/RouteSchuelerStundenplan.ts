import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SSchuelerStundenplan = () => import("~/components/schueler/stundenplan/SSchuelerStundenplan.vue");

export class RouteSchuelerStundenplan extends RouteNode<unknown> {

	public constructor() {
		super("schueler_stundenplan", "stundenplan", SSchuelerStundenplan);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl);
		super.text = "Stundenplan";
	}

}

export const routeSchuelerStundenplan = new RouteSchuelerStundenplan();

