import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "statistik",
	path: "/statistik",
	components: {
		default: () => import("./SStatistikApp.vue"),
		liste: () => import("./SStatistikAuswahl.vue")
	}
}