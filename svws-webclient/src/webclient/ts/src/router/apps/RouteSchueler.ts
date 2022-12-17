import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler";

export const RouteSchueler : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schueler/:id(\\d+)?",
	components: {
		default: () => import("~/components/schueler/SSchuelerApp.vue"),
		liste: () => import("~/components/schueler/SSchuelerAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.schueler.auswahl),
		liste: (route) => routePropsAuswahlID(route, ROUTE_NAME, injectMainApp().apps.schueler.auswahl)
	},
	meta: <RouteAppMeta<SchuelerListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.schueler.auswahl)
	}
}

