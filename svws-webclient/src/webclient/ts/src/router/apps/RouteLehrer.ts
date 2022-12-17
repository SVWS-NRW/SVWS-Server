import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "lehrer";

export const RouteLehrer : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/lehrer/:id(\\d+)?",
	components: {
		default: () => import("~/components/lehrer/SLehrerApp.vue"),
		liste: () => import("~/components/lehrer/SLehrerAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.lehrer.auswahl),
		liste: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.lehrer.auswahl)
	},
	meta: <RouteAppMeta<LehrerListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.lehrer.auswahl)
	}
};
