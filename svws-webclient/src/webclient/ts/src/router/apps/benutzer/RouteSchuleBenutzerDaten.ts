import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SBenutzer = () => import("~/components/schule/benutzerverwaltung/daten/SBenutzer.vue");

export class RouteSchuleBenutzerDaten extends RouteNode<unknown> {

	public constructor() {
		super("benutzer_daten", "daten", SBenutzer);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.benutzer.auswahl);
		super.text = "Daten";
	}

}

export const routeSchuleBenutzerDaten = new RouteSchuleBenutzerDaten();

