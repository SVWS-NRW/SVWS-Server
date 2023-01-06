import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { RouteKatalogFaecher } from "../RouteKatalogFaecher";

const SFachDaten = () => import("~/components/faecher/daten/SFachDaten.vue");

export class RouteFaecherDaten extends RouteNode<unknown, RouteKatalogFaecher> {

	public constructor() {
		super("faecher_daten", "daten", SFachDaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.faecher.auswahl);
		super.text = "Daten";
	}

}

export const routeFaecherDaten = new RouteFaecherDaten();

