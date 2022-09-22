import { RouteRecordRaw } from "vue-router";

export default <RouteRecordRaw>{
	name: "religionen",
	path: "/kataloge/religion/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("./SReligionenApp.vue"),
		liste: () => import("./SReligionenAuswahl.vue")
	}
}