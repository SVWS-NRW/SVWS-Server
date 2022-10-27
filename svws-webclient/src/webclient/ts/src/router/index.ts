import { createRouter, createWebHashHistory } from "vue-router";

import { mainApp } from "../apps/Main";
// Components
import Login from "~/components/SLogin.vue";
import SApp from "~/components/SApp.vue";

// Route Definitions
import SchuleRoute from "~/components/schule/SSchuleRoute";
import SLehrerRoute from "~/components/lehrer/SLehrerRoute";
import SKatalogeRoute from "~/components/kataloge/SKatalogeRoute";
import SFaecherRoute from "~/components/faecher/SFaecherRoute";
import SKatalogReligionRoute from "~/components/kataloge/religionen/SKatalogReligionRoute";
import SJahrgaengeRoute from "~/components/jahrgaenge/SJahrgaengeRoute";
import SKlassenRoute from "~/components/klassen/SKlassenRoute";
import SKurseRoute from "~/components/kurse/SKurseRoute";
import SGostRoute from "~/components/gost/SGostRoute";
import SSchuelerRoute from "~/components/schueler/SSchuelerRoute";
import SStatistikRoute from "~/components/statistik/SStatistikRoute";
import SBenutzerverwaltungRoute from "~/components/schule/benutzerverwaltung/SBenutzerverwaltungRoute";

export const router = createRouter({
	history: createWebHashHistory(import.meta.env.BASE_URL ?? "/"),
	routes: [
		{
			path: "/login",
			name: "login",
			component: Login
		},
		{
			path: "/",
			component: SApp,
			children: [
				// /schule
				SchuleRoute,
				// /schule/benutzerverwaltung
				SBenutzerverwaltungRoute,
				// /kataloge
				SKatalogeRoute,
				// /katalog/faecher/:fachId?
				SFaecherRoute,
				// /kataloge/religion/:religionId?
				SKatalogReligionRoute,
				// /kataloge/jahrgaenge/:jahrgangId?
				SJahrgaengeRoute,
				// /schueler/:schuelerId?
				SSchuelerRoute,
				// /lehrer/:lehrerId?
				SLehrerRoute,
				// /klassen/:klasseId?
				SKlassenRoute,
				// /kurse/:kursid?
				SKurseRoute,
				// /gost/:gostId?
				SGostRoute,
				// /statistik
				SStatistikRoute
			]
		}
	]
});

router.beforeEach(to => {
	if (
		// make sure the user is authenticated
		!mainApp.authenticated &&
		// ❗️ Avoid an infinite redirect
		to.name !== "login"
	) {
		// redirect the user to the login page
		return { name: "login", query: { redirect: to.fullPath } };
	}
});
