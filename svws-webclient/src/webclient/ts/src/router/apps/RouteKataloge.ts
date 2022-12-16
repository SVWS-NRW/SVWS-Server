import { RouteRecordRaw } from "vue-router";

export const RouteKataloge : RouteRecordRaw = {
	name: "kataloge",
	path: "/kataloge",
	components: {
		default: () => import("~/components/kataloge/SKatalogeApp.vue"),
		liste: () => import("~/components/kataloge/SKatalogeAuswahl.vue")
	}
}