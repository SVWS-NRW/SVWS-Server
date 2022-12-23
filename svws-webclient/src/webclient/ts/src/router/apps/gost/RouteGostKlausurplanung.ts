import { RouteLocationNormalizedLoaded, RouteRecordRaw, useRoute } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { routeAppMeta, RouteAppMeta } from "~/router/RouteUtils";
import { routePropsGostAuswahl } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanungKlausurdaten } from "./klausurplanung/RouteGostKlausurplanungKlausurdaten";
import { RouteGostKlausurplanungSchienen } from "./klausurplanung/RouteGostKlausurplanungSchienen";
import { RouteGostKlausurplanungKalender } from "./klausurplanung/RouteGostKlausurplanungKalender";
import { RouteGostKlausurplanungPlanung } from "./klausurplanung/RouteGostKlausurplanungPlanung";
import { RouteGostKlausurplanungKonflikte } from "./klausurplanung/RouteGostKlausurplanungKonflikte";
import { ref } from "vue";

const ROUTE_NAME: string = "gost_klausurplanung";

export const RouteGostKlausurplanungChildren: RouteRecordRaw[] = [
	RouteGostKlausurplanungKlausurdaten,
	RouteGostKlausurplanungSchienen,
	RouteGostKlausurplanungKalender,
	RouteGostKlausurplanungPlanung,
	RouteGostKlausurplanungKonflikte
];


export const RouteGostKlausurplanung : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "klausurplanung",
	component: () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue"),
	props: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => {
			const route = useRoute();
			return route.params.abiturjahr === "-1";
		},
		redirect: ref(RouteGostKlausurplanungKlausurdaten),
		text: "Klausurplanung"
	},
	redirect: to => {
		return to.path + "/" + routeAppMeta(RouteGostKlausurplanung)?.redirect.value.path;
	},
	children: RouteGostKlausurplanungChildren
};


export function routeGostKlausurplanungSetRedirect(route : RouteLocationNormalizedLoaded) {
	const meta = RouteGostKlausurplanung.meta as RouteAppMeta<unknown, unknown>;
	if ((RouteGostKlausurplanung.children === undefined) || (meta === undefined))
		return;
	for (var child of RouteGostKlausurplanung.children) {
		if (route.name === child.name) {
			meta.redirect.value = child;
			return;
		}
	}
	meta.redirect.value = RouteGostKlausurplanungKlausurdaten;
}


