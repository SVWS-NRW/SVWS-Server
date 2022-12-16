import { RouteRecordRaw } from "vue-router";

export const RouteKatalogJahrgaenge : RouteRecordRaw = {
	name: "jahrgaenge",
	path: "/kataloge/jahrgaenge/:id(\\d+)?",
	components: {
		default: () => import("~/components/jahrgaenge/SJahrgaengeApp.vue"),
		liste: () => import("~/components/jahrgaenge/SJahrgaengeAuswahl.vue")
	}
}