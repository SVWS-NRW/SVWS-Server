import { RouteRecordRaw, useRoute } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";
import { routePropsGostAuswahl } from "~/router/apps/RouteGost";

const ROUTE_NAME: string = "gost_klausurplanung_klausurdaten";

export const RouteGostKlausurplanungKlausurdaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "klausurdaten",
	component: () => import("~/components/gost/klausurplanung/SGostKlausurplanungDaten.vue"),
	props: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Klausurdaten"
	}
};
