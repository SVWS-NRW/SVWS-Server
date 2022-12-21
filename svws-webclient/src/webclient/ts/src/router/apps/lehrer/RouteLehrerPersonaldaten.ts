import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "lehrer_personaldaten";

export const RouteLehrerPersonaldaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "personaldaten",
	component: () => import("~/components/lehrer/personaldaten/SLehrerPersonaldaten.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		text: "Personaldaten"
	}
};
