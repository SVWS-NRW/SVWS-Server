import { RouteRecordRaw } from "vue-router";

export const RouteSchuleBenutzerverwaltung : RouteRecordRaw = {
	name: "benutzerverwaltung",
	path: "/schule/benutzerverwaltung",
	components: {
		default: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungApp.vue"),
		liste: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungAuswahl.vue")
	},
	children: [ 
		{
			name: "benutzer",
			path: "/schule/benutzerverwaltung/benutzer/:id?/:slug([a-zA-Z-0-9:]+)?",
			components: {
			}
		}, 
		{
			name: "benutzergruppe",
			path: "/schule/benutzerverwaltung/benutzergruppe/:id?/:slug([a-zA-Z-0-9:]+)?",
			components: {
			}
		}
	]
}