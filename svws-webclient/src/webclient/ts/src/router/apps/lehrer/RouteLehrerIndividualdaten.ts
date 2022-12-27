import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<unknown> {

	public constructor() {
		super("lehrer_daten", "daten", SLehrerIndividualdaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.lehrer.auswahl);
		super.text = "Daten";
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

