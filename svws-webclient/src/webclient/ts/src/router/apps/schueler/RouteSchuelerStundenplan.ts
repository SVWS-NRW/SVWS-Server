import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_stundenplan";

export const RouteSchuelerStundenplan : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "stundenplan",
	component: () => import("~/components/schueler/stundenplan/SSchuelerStundenplan.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Stundenplan"
	}
};
