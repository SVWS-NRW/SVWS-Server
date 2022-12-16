import { RouteRecordRaw } from "vue-router";

export const RouteGost : RouteRecordRaw = {
	name: "gost",
	path: "/gost/:id?",
	components: {
		default: () => import("~/components/gost/SGostApp.vue"),
		liste: () => import("~/components/gost/SGostAuswahl.vue")
	}
}