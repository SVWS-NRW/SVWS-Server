import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_adressen";

export const RouteSchuelerAdressen : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "adressen",
	component: () => import("~/components/schueler/adressen/SSchuelerAdressen.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Adressen / Betriebe"
	}
};
