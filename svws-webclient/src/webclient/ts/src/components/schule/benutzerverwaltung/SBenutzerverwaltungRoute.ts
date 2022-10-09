import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "benutzerverwaltung",
	path: "/schule/benutzerverwaltung",
	components: {
		default: () => import("./SBenutzerverwaltungApp.vue"),
		liste: () => import("./SBenutzerverwaltungAuswahl.vue")
	}
}