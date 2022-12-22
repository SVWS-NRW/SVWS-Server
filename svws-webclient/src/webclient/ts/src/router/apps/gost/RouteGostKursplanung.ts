import { RouteRecordRaw, useRoute } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";
import { routePropsGostAuswahl } from "~/router/apps/RouteGost";

const ROUTE_NAME: string = "gost_kursplanung";

export const RouteGostKursplanung : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "kursplanung",
	component: () => import("~/components/gost/kursplanung/SGostKursplanung.vue"),
	props: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => {
			const route = useRoute();
			return route.params.abiturjahr === "-1";
		},
		text: "Kursplanung"
	}
};
