import { BenutzergruppeListeEintrag, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";


export const RouteSchuleBenutzerverwaltungBenutzer : RouteRecordRaw = {
	name: "benutzer",
	path: "/schule/benutzerverwaltung/benutzer/:id(\\d+)?",
	components: {
	},
	props: {
		default: (route) => routePropsAuswahlID(route, "benutzer", injectMainApp().apps.benutzer.auswahl),
		liste: (route) => routePropsAuswahlID(route, "benutzer", injectMainApp().apps.benutzer.auswahl)
	},
	meta: <RouteAppMeta<BenutzerListeEintrag  | undefined>> {
		auswahl: () => routeAuswahlID("benutzer", injectMainApp().apps.benutzer.auswahl)
	}
}

export const RouteSchuleBenutzerverwaltungBenutzergruppe : RouteRecordRaw = {
	name: "benutzergruppe",
	path: "/schule/benutzerverwaltung/benutzergruppe/:id(\\d+)?",
	components: {
	},
	props: {
		default: (route) => routePropsAuswahlID(route, "benutzergruppe", injectMainApp().apps.benutzergruppe.auswahl),
		liste: (route) => routePropsAuswahlID(route, "benutzergruppe", injectMainApp().apps.benutzergruppe.auswahl)
	},
	meta: <RouteAppMeta<BenutzergruppeListeEintrag  | undefined>> {
		auswahl: () => routeAuswahlID("benutzergruppe", injectMainApp().apps.benutzergruppe.auswahl)
	}
}

export const RouteSchuleBenutzerverwaltung : RouteRecordRaw = {
	name: "benutzerverwaltung",
	path: "/schule/benutzerverwaltung",
	components: {
		default: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungApp.vue"),
		liste: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungAuswahl.vue")
	},
	children: [ RouteSchuleBenutzerverwaltungBenutzer, RouteSchuleBenutzerverwaltungBenutzergruppe ]
}
