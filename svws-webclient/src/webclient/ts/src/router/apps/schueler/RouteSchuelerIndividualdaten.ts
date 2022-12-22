import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_daten";

export const RouteSchuelerIndividualdaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "daten",
	component: () => import("~/components/schueler/individualdaten/SSchuelerIndividualdaten.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Individualdaten"
	}
};
