import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "lehrer_daten";

export const RouteLehrerIndividualdaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "daten",
	component: () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl),
	meta: <RouteAppMeta<undefined>> {
		auswahl: () => {},
		text: "Daten"
	}
};
