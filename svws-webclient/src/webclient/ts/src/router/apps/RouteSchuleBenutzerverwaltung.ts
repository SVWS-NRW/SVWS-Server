import { RouteRecordRaw } from "vue-router";
import { RouteSchuleBenutzerverwaltungBenutzer } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzer";
import { RouteSchuleBenutzerverwaltungBenutzergruppe } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzergruppe";

const ROUTE_NAME: string = "benutzerverwaltung";

export const RouteSchuleBenutzerverwaltung : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung",
	components: {
		default: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungApp.vue"),
		liste: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungAuswahl.vue")
	},
	children: [ RouteSchuleBenutzerverwaltungBenutzer, RouteSchuleBenutzerverwaltungBenutzergruppe ]
}
