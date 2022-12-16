import { RouteRecordRaw } from "vue-router";

export const RouteLehrer : RouteRecordRaw = {
	name: "lehrer",
	path: "/lehrer/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("~/components/lehrer/SLehrerApp.vue"),
		liste: () => import("~/components/lehrer/SLehrerAuswahl.vue")
	}
};