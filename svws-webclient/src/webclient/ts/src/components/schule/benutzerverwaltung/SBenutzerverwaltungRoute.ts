import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "benutzerverwaltung",
	path: "/schule/benutzerverwaltung",
	components: {
		default: () => import("./SBenutzerverwaltungApp.vue"),
		liste: () => import("./SBenutzerverwaltungAuswahl.vue")
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