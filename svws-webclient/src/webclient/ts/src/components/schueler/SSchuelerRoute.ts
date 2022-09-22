import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "schueler",
	path: "/schueler/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SSchuelerApp.vue"),
		liste: () => import("./SSchuelerAuswahl.vue")
	}
}