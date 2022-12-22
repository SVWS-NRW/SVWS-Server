import { RouteRecordRaw, useRoute } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";
import { routePropsGostAuswahl } from "~/router/apps/RouteGost";

const ROUTE_NAME: string = "gost_fachwahlen";

export const RouteGostFachwahlen : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "fachwahlen",
	component: () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue"),
	props: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => {
			const route = useRoute();
			return route.params.abiturjahr === "-1";
		},
		text: "Fachwahlen"
	}
};
