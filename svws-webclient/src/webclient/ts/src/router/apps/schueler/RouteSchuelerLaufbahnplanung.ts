import { App } from "~/apps/BaseApp";
import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SSchuelerLaufbahnplanung = () => import("~/components/schueler/laufbahnplanung/SSchuelerLaufbahnplanung.vue");

export class RouteSchuelerLaufbahnplanung extends RouteNode<unknown> {

	public constructor() {
		super("schueler_laufbahnplanung", "laufbahnplanung", SSchuelerLaufbahnplanung);
		super.propHandler = (route) => RouteNodeListView.getPropsByAuswahlID(route, mainApp.apps.schueler.auswahl);
		super.text = "Laufbahnplanung";
		super.isHidden = () => {
			const abiturjahr = mainApp.apps.schueler.dataGostLaufbahndaten?.daten?.abiturjahr;   // TODO Bestimme Abiturjahr über Daten aus RouteSchueler
			const jahrgang = App.apps.gost.auswahl.liste.find(j => (j.abiturjahr === abiturjahr));   // TODO Bestimme Jahrgang aus der Liste der Abiturjahgänge in RouteSchueler
			return (jahrgang === undefined);
		}
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

