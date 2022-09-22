import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "faecher",
	path: "/kataloge/faecher/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SFaecherApp.vue"),
		liste: () => import("./SFaecherAuswahl.vue")
	}
}
