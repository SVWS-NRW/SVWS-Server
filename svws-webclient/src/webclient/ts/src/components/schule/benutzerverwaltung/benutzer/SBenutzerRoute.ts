import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "benutzer",
	path: "/schule/benutzer/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SBenutzerApp.vue"),
		liste: () => import("./SBenutzerAuswahl.vue")
	}
}