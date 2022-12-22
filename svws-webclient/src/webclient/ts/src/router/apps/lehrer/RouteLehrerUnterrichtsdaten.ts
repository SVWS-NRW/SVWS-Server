import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "lehrer_unterrichtsdaten";

export const RouteLehrerUnterrichtsdaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "unterrichtsdaten",
	component: () => import("~/components/lehrer/unterrichtsdaten/SLehrerUnterrichtsdaten.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Unterrichtsdaten"
	}
};
