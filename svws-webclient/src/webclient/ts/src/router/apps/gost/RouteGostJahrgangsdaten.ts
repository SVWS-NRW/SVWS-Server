import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";
import { routePropsGostAuswahl } from "~/router/apps/RouteGost";

const ROUTE_NAME: string = "gost_jahrgangsdaten";

export const RouteGostJahrgangsdaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "daten",
	component: () => import("~/components/gost/stammdaten/SGostStammdaten.vue"),
	props: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Allgemein"
	}
};
