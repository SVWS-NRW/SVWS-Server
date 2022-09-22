import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "klassen",
	path: "/klassen/:id(\\d+)?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SKlassenApp.vue"),
		liste: () => import("./SKlassenAuswahl.vue")
	}
}