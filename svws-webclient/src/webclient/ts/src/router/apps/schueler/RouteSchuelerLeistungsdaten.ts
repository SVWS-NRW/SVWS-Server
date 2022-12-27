import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";


const SSchuelerLeistungsdaten = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungsdaten.vue");

export class RouteSchuelerLeistungsdaten extends RouteNode<unknown> {

	public constructor() {
		super("schueler_leistungsdaten", "leistungsdaten", SSchuelerLeistungsdaten);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl);
		super.text = "Leistungsdaten";
	}

}

export const routeSchuelerLeistungsdaten = new RouteSchuelerLeistungsdaten();

