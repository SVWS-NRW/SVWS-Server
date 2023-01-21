import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { RouteSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";

const SBenutzergruppe = () => import("~/components/schule/benutzergruppen/daten/SBenutzergruppe.vue");

export class RouteSchuleBenutzergruppeDaten extends RouteNode<unknown, RouteSchuleBenutzer> {

	public constructor() {
		super("benutzergruppe_daten", "daten", SBenutzergruppe);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.benutzergruppe.auswahl);
		super.text = "Daten";
	}

}

export const routeSchuleBenutzergruppeDaten = new RouteSchuleBenutzergruppeDaten();

