import { RouteRecordRaw } from "vue-router";
import { RouteAppMeta } from "~/router/RouteUtils";

const ROUTE_NAME: string = "statistik";

export const RouteStatistik : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/statistik",
	components: {
		default: () => import("~/components/statistik/SStatistikApp.vue"),
		liste: () => import("~/components/statistik/SStatistikAuswahl.vue")
	},
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {}
	}
}