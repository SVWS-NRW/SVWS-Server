import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "kataloge",
	path: "/kataloge",
	components: {
		default: () => import("./SKatalogeApp.vue"),
		liste: () => import("./SKatalogeAuswahl.vue")
	}
}