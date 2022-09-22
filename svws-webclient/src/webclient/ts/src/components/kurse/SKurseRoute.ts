import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "kurse",
	path: "/kurse/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SKurseApp.vue"),
		liste: () => import("./SKurseAuswahl.vue")
	}
}