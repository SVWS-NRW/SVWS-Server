import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdaten.vue");

export class RouteSchuelerIndividualdaten extends RouteNode<unknown> {

	public constructor() {
		super("schueler_daten", "daten", SSchuelerIndividualdaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl);
		super.text = "Individualdaten";
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

