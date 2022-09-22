import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "jahrgaenge",
	path: "/kataloge/jahrgaenge/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SJahrgaengeApp.vue"),
		liste: () => import("./SJahrgaengeAuswahl.vue")
	}
}