import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_schulbesuch";

export const RouteSchuelerSchulbesuch : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "schulbesuch",
	component: () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Schulbesuch"
	}
};
