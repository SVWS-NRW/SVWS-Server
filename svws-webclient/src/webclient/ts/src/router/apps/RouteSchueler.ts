import { RouteRecordRaw } from "vue-router";

export const RouteSchueler : RouteRecordRaw = {
	name: "schueler",
	path: "/schueler/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("~/components/schueler/SSchuelerApp.vue"),
		liste: () => import("~/components/schueler/SSchuelerAuswahl.vue")
	}
}