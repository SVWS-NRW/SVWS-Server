import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "lehrer",
	path: "/lehrer/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SLehrerApp.vue"),
		liste: () => import("./SLehrerAuswahl.vue")
	}
};