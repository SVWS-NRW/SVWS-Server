import { KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "klassen";

export const RouteKlassen : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/klassen/:id(\\d+)?",
	components: {
		default: () => import("~/components/klassen/SKlassenApp.vue"),
		liste: () => import("~/components/klassen/SKlassenAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, injectMainApp().apps.klassen.auswahl),
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.klassen.auswahl)
	},
	meta: <RouteAppMeta<KlassenListeEintrag | undefined, unknown>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.klassen.auswahl)
	}
}