import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SJahrgangDaten = () => import("~/components/jahrgaenge/daten/SJahrgangDaten.vue");

export class RouteKatalogJahrgaengeDaten extends RouteNode<unknown> {

	public constructor() {
		super("jahrgaenge_daten", "daten", SJahrgangDaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.jahrgaenge.auswahl);
		super.text = "Daten";
	}

}

export const routeKatalogJahrgaengeDaten = new RouteKatalogJahrgaengeDaten();

