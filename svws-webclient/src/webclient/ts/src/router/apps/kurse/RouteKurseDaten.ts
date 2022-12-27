import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SKursDaten = () => import("~/components/kurse/daten/SKursDaten.vue");

export class RouteKurseDaten extends RouteNode<unknown> {

	public constructor() {
		super("kurse_daten", "daten", SKursDaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.kurse.auswahl);
		super.text = "Daten";
	}

}

export const routeKurseDaten = new RouteKurseDaten();

