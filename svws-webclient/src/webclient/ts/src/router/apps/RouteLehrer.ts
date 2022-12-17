import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";


export const RouteLehrer : RouteRecordRaw = {
	name: "lehrer",
	path: "/lehrer/:id(\\d+)?",
	components: {
		default: () => import("~/components/lehrer/SLehrerApp.vue"),
		liste: () => import("~/components/lehrer/SLehrerAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, "lehrer", injectMainApp().apps.lehrer.auswahl),
		liste: (route) => routePropsAuswahlID(route, "lehrer", injectMainApp().apps.lehrer.auswahl)
	},
	meta: <RouteAppMeta<LehrerListeEintrag | undefined>> {
		auswahl: () => routeAuswahlID("lehrer", injectMainApp().apps.lehrer.auswahl)
	}
};
