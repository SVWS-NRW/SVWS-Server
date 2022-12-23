import { RouteRecordRaw, useRoute } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";
import { routePropsGostAuswahl } from "~/router/apps/RouteGost";

const ROUTE_NAME: string = "gost_klausurplanung_schienen";

export const RouteGostKlausurplanungSchienen : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "schienen",
	component: () => import("~/components/gost/klausurplanung/SGostKlausurplanungSchienen.vue"),
	props: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Schienen"
	}
};
