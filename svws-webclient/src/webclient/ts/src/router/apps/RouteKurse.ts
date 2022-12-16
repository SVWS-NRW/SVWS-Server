import { RouteRecordRaw } from "vue-router";

export const RouteKurse : RouteRecordRaw = {
	name: "kurse",
	path: "/kurse/:id(\\d+)?",
	components: {
		default: () => import("~/components/kurse/SKurseApp.vue"),
		liste: () => import("~/components/kurse/SKurseAuswahl.vue")
	}
}