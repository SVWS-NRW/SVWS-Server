import { RouteRecordRaw } from "vue-router";

export const RouteKlassen : RouteRecordRaw = {
	name: "klassen",
	path: "/klassen/:id(\\d+)?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("~/components/klassen/SKlassenApp.vue"),
		liste: () => import("~/components/klassen/SKlassenAuswahl.vue")
	}
}