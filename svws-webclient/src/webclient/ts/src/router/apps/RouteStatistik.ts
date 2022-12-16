import { RouteRecordRaw } from "vue-router";

export const RouteStatistik : RouteRecordRaw = {
	name: "statistik",
	path: "/statistik",
	components: {
		default: () => import("~/components/statistik/SStatistikApp.vue"),
		liste: () => import("~/components/statistik/SStatistikAuswahl.vue")
	}
}