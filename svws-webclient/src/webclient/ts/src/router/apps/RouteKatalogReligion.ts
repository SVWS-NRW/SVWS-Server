import { RouteRecordRaw } from "vue-router";

export const RouteKatalogReligion : RouteRecordRaw = {
	name: "religionen",
	path: "/kataloge/religion/:id?/:slug([a-zA-Z-0-9:]+)?",
	components: {
		default: () => import("~/components/kataloge/religionen/SReligionenApp.vue"),
		liste: () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
	}
}