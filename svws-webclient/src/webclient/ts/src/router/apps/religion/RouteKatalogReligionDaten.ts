import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SReligionDaten = () => import("~/components/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<unknown> {

	public constructor() {
		super("religionen_daten", "daten", SReligionDaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.religionen.auswahl);
		super.text = "Daten";
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

