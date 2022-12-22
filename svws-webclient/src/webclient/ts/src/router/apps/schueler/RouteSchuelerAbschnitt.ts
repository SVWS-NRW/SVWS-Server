import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_abschnitt";

export const RouteSchuelerAbschnitt : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "abschnitt",
	component: () => import("~/components/schueler/abschnitt/SSchuelerAbschnitt.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Aktueller Abschnitt"   // TODO Quartal, etc. aus den Daten der eigenen Schule
	}
};
