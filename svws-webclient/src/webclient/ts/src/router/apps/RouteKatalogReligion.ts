import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "religionen";

export const RouteKatalogReligion : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/kataloge/religion/:id(\\d+)?",
	components: {
		default: () => import("~/components/kataloge/religionen/SReligionenApp.vue"),
		liste: () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, injectMainApp().apps.religionen.auswahl),
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.religionen.auswahl)
	},
	meta: <RouteAppMeta<ReligionEintrag | undefined, unknown>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.religionen.auswahl)
	}
}