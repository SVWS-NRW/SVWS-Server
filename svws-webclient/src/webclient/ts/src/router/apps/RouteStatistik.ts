import { RouteRecordRaw } from "vue-router";
import { RouteAppMeta } from "~/router/RouteUtils";

export const RouteStatistik : RouteRecordRaw = {
	name: "statistik",
	path: "/statistik",
	components: {
		default: () => import("~/components/statistik/SStatistikApp.vue"),
		liste: () => import("~/components/statistik/SStatistikAuswahl.vue")
	},
	meta: <RouteAppMeta<undefined>> {
		auswahl: () => {}
	}
}