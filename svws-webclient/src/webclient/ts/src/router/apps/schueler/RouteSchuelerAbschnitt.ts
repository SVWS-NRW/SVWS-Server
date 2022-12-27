import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";


const SSchuelerAbschnitt = () => import("~/components/schueler/abschnitt/SSchuelerAbschnitt.vue");

export class RouteSchuelerAbschnitt extends RouteNode<unknown> {

	public constructor() {
		super("schueler_abschnitt", "abschnitt", SSchuelerAbschnitt);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl);
		super.text = "Aktueller Abschnitt";
	}

}

export const routeSchuelerAbschnitt = new RouteSchuelerAbschnitt();

