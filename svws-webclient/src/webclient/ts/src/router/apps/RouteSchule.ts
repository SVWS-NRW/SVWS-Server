import { RouteRecordRaw } from "vue-router";
import { RouteAppMeta } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schule";

export const RouteSchule : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule",
	components: {
		default: () => import("~/components/schule/SSchuleApp.vue"),
		liste: () => import("~/components/schule/SSchuleAuswahl.vue")
	},
	meta: <RouteAppMeta<undefined>> {
		auswahl: () => {}
	}
}