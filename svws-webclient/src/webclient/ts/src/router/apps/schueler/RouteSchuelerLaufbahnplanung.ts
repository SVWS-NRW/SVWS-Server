import { RouteRecordRaw } from "vue-router";
import { App } from "~/apps/BaseApp";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_laufbahnplanung";

export const RouteSchuelerLaufbahnplanung : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "laufbahnplanung",
	component: () => import("~/components/schueler/laufbahnplanung/SSchuelerLaufbahnplanung.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => {
			const abiturjahr = injectMainApp().apps.schueler.dataGostLaufbahndaten?.daten?.abiturjahr; // TODO Bestimme Abiturjahr über Daten aus RouteSchueler
			const jahrgang = App.apps.gost.auswahl.liste.find(j => (j.abiturjahr === abiturjahr));     // TODO Bestimme Jahrgang aus der Liste der Abiturjahgänge in RouteSchueler
			return (jahrgang === undefined);
		},
		text: "Laufbahnplanung"
	}
};
