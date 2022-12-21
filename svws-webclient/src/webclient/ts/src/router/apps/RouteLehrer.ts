import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalizedLoaded, RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";
import { RouteLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { RouteLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { RouteLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";

const ROUTE_NAME: string = "lehrer";

export const RouteLehrerChildren: RouteRecordRaw[] = [
	RouteLehrerIndividualdaten,
	RouteLehrerPersonaldaten,
	RouteLehrerUnterrichtsdaten
];

export const RouteLehrer : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/lehrer/:id(\\d+)?",
	components: {
		default: () => import("~/components/lehrer/SLehrerApp.vue"),
		liste: () => import("~/components/lehrer/SLehrerAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl),
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl)
	},
	meta: <RouteAppMeta<LehrerListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.lehrer.auswahl),
		redirect: RouteLehrerIndividualdaten
	},
	redirect: to => {
		return to.path + "/" + routeAppMeta(RouteLehrer)?.redirect.path;
	},
	children: RouteLehrerChildren
};


export function routeLehrerSetRedirect(route : RouteLocationNormalizedLoaded) {
	const meta = RouteLehrer.meta;
	if (meta === undefined)
		return;
	if (route.name === RouteLehrerPersonaldaten.name) {
		meta.redirect = RouteLehrerPersonaldaten;
	} else if (route.name === RouteLehrerUnterrichtsdaten.name) {
		meta.redirect = RouteLehrerUnterrichtsdaten;
	} else {
		meta.redirect = RouteLehrerIndividualdaten;
	}
}