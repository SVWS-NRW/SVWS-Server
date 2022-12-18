import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "jahrgaenge";

export const RouteKatalogJahrgaenge : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/kataloge/jahrgaenge/:id(\\d+)?",
	components: {
		default: () => import("~/components/jahrgaenge/SJahrgaengeApp.vue"),
		liste: () => import("~/components/jahrgaenge/SJahrgaengeAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, injectMainApp().apps.jahrgaenge.auswahl),
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.jahrgaenge.auswahl)
	},
	meta: <RouteAppMeta<JahrgangsListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.jahrgaenge.auswahl)
	}
}