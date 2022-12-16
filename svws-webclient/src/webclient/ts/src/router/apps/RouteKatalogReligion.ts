import { RouteRecordRaw } from "vue-router";

export const RouteKatalogReligion : RouteRecordRaw = {
	name: "religionen",
	path: "/kataloge/religion/:id(\\d+)?",
	components: {
		default: () => import("~/components/kataloge/religionen/SReligionenApp.vue"),
		liste: () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
	}
}