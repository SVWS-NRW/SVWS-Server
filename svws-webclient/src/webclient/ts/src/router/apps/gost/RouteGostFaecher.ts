import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";
import { routePropsGostAuswahl } from "~/router/apps/RouteGost";

const ROUTE_NAME: string = "gost_faecher";

export const RouteGostFaecher : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "faecher",
	component: () => import("~/components/gost/faecher/SGostFaecher.vue"),
	props: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Fächer"
	}
};
