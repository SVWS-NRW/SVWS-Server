import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SKlassenDaten = () => import("~/components/klassen/daten/SKlassenDaten.vue");

export class RouteKlassenDaten extends RouteNode<unknown> {

	public constructor() {
		super("klassen_daten", "daten", SKlassenDaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.klassen.auswahl);
		super.text = "Daten";
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();

