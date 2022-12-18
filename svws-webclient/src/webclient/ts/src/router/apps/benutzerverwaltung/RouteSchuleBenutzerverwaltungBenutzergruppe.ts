import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "benutzergruppe";

export const RouteSchuleBenutzerverwaltungBenutzergruppe : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung/benutzergruppe/:id(\\d+)?",
	components: {
	},
	props: {
		default: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzergruppe.auswahl),
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzergruppe.auswahl)
	},
	meta: <RouteAppMeta<BenutzergruppeListeEintrag  | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.benutzergruppe.auswahl)
	}
}
