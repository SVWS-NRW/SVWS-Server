import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "schule",
	path: "/schule",
	components: {
		default: () => import("./SSchuleApp.vue"),
		liste: () => import("./SSchuleAuswahl.vue")
	}
}