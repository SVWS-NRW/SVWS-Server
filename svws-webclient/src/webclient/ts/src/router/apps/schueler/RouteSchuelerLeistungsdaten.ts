import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_leistungsdaten";

export const RouteSchuelerLeistungsdaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "leistungsdaten",
	component: () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungsdaten.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Leistungsdaten"
	}
};
