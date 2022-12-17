import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";


export const RouteSchueler : RouteRecordRaw = {
	name: "schueler",
	path: "/schueler/:id(\\d+)?",
	components: {
		default: () => import("~/components/schueler/SSchuelerApp.vue"),
		liste: () => import("~/components/schueler/SSchuelerAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, "schueler", injectMainApp().apps.schueler.auswahl),
		liste: (route) => routePropsAuswahlID(route, "schueler", injectMainApp().apps.schueler.auswahl)
	},
	meta: <RouteAppMeta<SchuelerListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID("schueler", injectMainApp().apps.schueler.auswahl)
	}
}

