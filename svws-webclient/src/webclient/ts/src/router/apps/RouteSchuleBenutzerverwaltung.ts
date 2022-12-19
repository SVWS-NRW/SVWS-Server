import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";
import { RouteSchuleBenutzerverwaltungBenutzer } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzer";
import { RouteSchuleBenutzerverwaltungBenutzergruppe } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzergruppe";

const ROUTE_NAME: string = "benutzerverwaltung";


export const RoutesBenutzerverwaltungChildren: RouteRecordRaw[] = [
	RouteSchuleBenutzerverwaltungBenutzer,
	RouteSchuleBenutzerverwaltungBenutzergruppe
];

export const RouteSchuleBenutzerverwaltung : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung/:id(\\d+)?",
	components: {
		default: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungApp.vue"),
		liste: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungAuswahl.vue")
	},
	props: {
		default: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzer.auswahl),
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzer.auswahl)
	},
	meta: <RouteAppMeta<undefined>> {
		auswahl: () => {}
	},
	redirect: to => to.path + "/benutzer",
	children: RoutesBenutzerverwaltungChildren
}
