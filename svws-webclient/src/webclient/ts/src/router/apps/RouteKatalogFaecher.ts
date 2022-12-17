import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "faecher";

export const RouteKatalogFaecher : RouteRecordRaw = {
	name: "faecher",
	path: "/kataloge/faecher/:id(\\d+)?",
	components: {
		default: () => import("~/components/faecher/SFaecherApp.vue"),
		liste: () => import("~/components/faecher/SFaecherAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.faecher.auswahl),
		liste: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.faecher.auswahl)
	},
	meta: <RouteAppMeta<FaecherListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.faecher.auswahl)
	}
}
