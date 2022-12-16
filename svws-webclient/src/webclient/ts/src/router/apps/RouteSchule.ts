import { RouteRecordRaw } from "vue-router";

export const RouteSchule : RouteRecordRaw = {
	name: "schule",
	path: "/schule",
	components: {
		default: () => import("~/components/schule/SSchuleApp.vue"),
		liste: () => import("~/components/schule/SSchuleAuswahl.vue")
	}
}