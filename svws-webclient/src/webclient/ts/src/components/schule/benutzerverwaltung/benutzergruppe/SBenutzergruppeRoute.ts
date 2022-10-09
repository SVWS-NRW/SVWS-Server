import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "benutzergruppe",
	path: "/schule/benutzergruppe/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SBenutzergruppeApp.vue"),
		liste: () => import("./SBenutzergruppeAuswahl.vue")
	}
}