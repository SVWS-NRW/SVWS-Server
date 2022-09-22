import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "gost",
	path: "/gost/:id?",
	components: {
		default: () => import("./SGostApp.vue"),
		liste: () => import("./SGostAuswahl.vue")
	}
}