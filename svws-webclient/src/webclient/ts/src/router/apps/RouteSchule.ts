import { RouteRecordRaw } from "vue-router";
import { RouteAppMeta } from "../RouteAppMeta";

export const RouteSchule : RouteRecordRaw = {
	name: "schule",
	path: "/schule",
	components: {
		default: () => import("~/components/schule/SSchuleApp.vue"),
		liste: () => import("~/components/schule/SSchuleAuswahl.vue")
	},
	meta: <RouteAppMeta<undefined>> {
		auswahl: () => {}
	}
}