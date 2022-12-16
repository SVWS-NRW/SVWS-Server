import { RouteRecordRaw } from "vue-router";

export const RouteKatalogJahrgaenge : RouteRecordRaw = {
	name: "jahrgaenge",
	path: "/kataloge/jahrgaenge/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("~/components/jahrgaenge/SJahrgaengeApp.vue"),
		liste: () => import("~/components/jahrgaenge/SJahrgaengeAuswahl.vue")
	}
}