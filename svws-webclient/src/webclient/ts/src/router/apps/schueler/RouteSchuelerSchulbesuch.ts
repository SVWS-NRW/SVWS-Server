import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

export class RouteSchuelerSchulbesuch extends RouteNode<unknown> {

	public constructor() {
		super("schueler_schulbesuch", "schulbesuch", SSchuelerSchulbesuch);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl);
		super.text = "Schulbesuch";
	}

}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();

