import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SFoerderschwerpunktDaten = () => import("~/components/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunktDaten.vue");

export class RouteKatalogFoerderschwerpunkteDaten extends RouteNode<unknown> {

	public constructor() {
		super("foerderschwerpunkte_daten", "daten", SFoerderschwerpunktDaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.foerderschwerpunkte.auswahl);
		super.text = "Daten";
	}

}

export const routeKatalogFoerderschwerpunkteDaten = new RouteKatalogFoerderschwerpunkteDaten();

