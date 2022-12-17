import { RouteRecordRaw } from "vue-router";
import { RouteAppMeta } from "~/router/RouteUtils";

const ROUTE_NAME: string = "kataloge";

export const RouteKataloge : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/kataloge",
	components: {
		default: () => import("~/components/kataloge/SKatalogeApp.vue"),
		liste: () => import("~/components/kataloge/SKatalogeAuswahl.vue")
	},
	meta: <RouteAppMeta<undefined>> {
		auswahl: () => {}
	}
}