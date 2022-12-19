import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "benutzergruppe";

export const RouteSchuleBenutzerverwaltungBenutzergruppe : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung/:id(\\d+)?/benutzergruppe",
	component: () => import("~/components/schule/benutzerverwaltung/daten/SBenutzergruppe.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzer.auswahl),
	meta: <RouteAppMeta<BenutzergruppeListeEintrag  | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.benutzergruppe.auswahl),
		text: "Benutzergruppe"
	}
}
