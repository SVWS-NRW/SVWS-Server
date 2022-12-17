import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "kurse";

export const RouteKurse : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/kurse/:id(\\d+)?",
	components: {
		default: () => import("~/components/kurse/SKurseApp.vue"),
		liste: () => import("~/components/kurse/SKurseAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.kurse.auswahl),
		liste: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.kurse.auswahl)
	},
	meta: <RouteAppMeta<KursListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.kurse.auswahl)
	}
}