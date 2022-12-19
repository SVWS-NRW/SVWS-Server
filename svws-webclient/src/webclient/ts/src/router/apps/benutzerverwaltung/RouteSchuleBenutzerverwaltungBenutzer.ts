import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "benutzer";

export const RouteSchuleBenutzerverwaltungBenutzer : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung/:id(\\d+)?/benutzer",
	component: () => import("~/components/schule/benutzerverwaltung/daten/SBenutzer.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzer.auswahl),
	meta: <RouteAppMeta<BenutzerListeEintrag  | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.benutzer.auswahl),
		text: "Benutzer"
	}
}
