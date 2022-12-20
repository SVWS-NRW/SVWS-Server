import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "foerderschwerpunkte",
	path: "/kataloge/foerderschwerpunkte",
	components: {
		default: () => import("./SFoerderschwerpunkteApp.vue"),
		liste: () => import("./SFoerderschwerpunkteAuswahl.vue")
	}
}
