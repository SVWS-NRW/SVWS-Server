import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalizedLoaded, RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { routeAppData, RouteAppMeta, routeAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";
import { RouteLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { RouteLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { RouteLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { ref } from "vue";
import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";

const ROUTE_NAME: string = "lehrer";

export const RouteLehrerChildren: RouteRecordRaw[] = [
	RouteLehrerIndividualdaten,
	RouteLehrerPersonaldaten,
	RouteLehrerUnterrichtsdaten
];


export interface RouteDataLehrer {
	item: LehrerListeEintrag | undefined;
	stammdaten: DataLehrerStammdaten;
}

export const RouteLehrer : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/lehrkraefte/:id(\\d+)?",
	components: {
		default: () => import("~/components/lehrer/SLehrerApp.vue"),
		liste: () => import("~/components/lehrer/SLehrerAuswahl.vue")
	},
	props: {
		default: (route) => {
			const prop = routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl);
			const data: RouteDataLehrer = routeAppData(RouteLehrer);
			if (prop.item !== data.item) {
				if (prop.item === undefined) {
					data.item = undefined;
					data.stammdaten.unselect();
				} else {
					data.item = prop.item as LehrerListeEintrag;
					data.stammdaten.select(data.item);
				}
			}
			return prop;
		},
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl)
	},
	meta: <RouteAppMeta<LehrerListeEintrag | undefined, RouteDataLehrer>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.lehrer.auswahl),
		redirect: ref(RouteLehrerIndividualdaten),
		data: <RouteDataLehrer>{
			item: undefined,
			stammdaten: new DataLehrerStammdaten()
		}
	},
	redirect: to => {
		return to.path + "/" + routeAppMeta(RouteLehrer)?.redirect.value.path;
	},
	children: RouteLehrerChildren
};


export function routeLehrerSetRedirect(route : RouteLocationNormalizedLoaded) {
	const meta = RouteLehrer.meta as RouteAppMeta<LehrerListeEintrag | undefined, RouteDataLehrer>;
	if (meta === undefined)
		return;
	if (route.name === RouteLehrerPersonaldaten.name) {
		meta.redirect.value = RouteLehrerPersonaldaten;
	} else if (route.name === RouteLehrerUnterrichtsdaten.name) {
		meta.redirect.value = RouteLehrerUnterrichtsdaten;
	} else {
		meta.redirect.value = RouteLehrerIndividualdaten;
	}
}