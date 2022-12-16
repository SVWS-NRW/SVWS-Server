import { RouteRecordRaw } from "vue-router";

export const RouteKlassen : RouteRecordRaw = {
	name: "klassen",
	path: "/klassen/:id(\\d+)?",
	components: {
		default: () => import("~/components/klassen/SKlassenApp.vue"),
		liste: () => import("~/components/klassen/SKlassenAuswahl.vue")
	}
}