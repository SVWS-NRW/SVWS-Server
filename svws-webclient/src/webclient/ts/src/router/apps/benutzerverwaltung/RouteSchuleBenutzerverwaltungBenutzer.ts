import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "benutzer";

export const RouteSchuleBenutzerverwaltungBenutzer : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung/benutzer/:id(\\d+)?",
	components: {
	},
	props: {
		default: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzer.auswahl),
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzer.auswahl)
	},
	meta: <RouteAppMeta<BenutzerListeEintrag  | undefined>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.benutzer.auswahl)
	}
}
