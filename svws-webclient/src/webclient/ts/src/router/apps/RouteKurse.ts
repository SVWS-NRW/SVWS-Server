import { RouteRecordRaw } from "vue-router";

export const RouteKurse : RouteRecordRaw = {
	name: "kurse",
	path: "/kurse/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("~/components/kurse/SKurseApp.vue"),
		liste: () => import("~/components/kurse/SKurseAuswahl.vue")
	}
}