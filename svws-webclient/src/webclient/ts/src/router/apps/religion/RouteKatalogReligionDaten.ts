import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { RouteKatalogReligion } from "../RouteKatalogReligion";

const SReligionDaten = () => import("~/components/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<unknown, RouteKatalogReligion> {

	public constructor() {
		super("religionen_daten", "daten", SReligionDaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.religionen.auswahl);
		super.text = "Daten";
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

