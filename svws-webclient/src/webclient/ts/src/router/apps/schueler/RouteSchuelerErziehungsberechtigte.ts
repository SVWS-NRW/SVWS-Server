import { RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "schueler_erziehungsberechtigte";

export const RouteSchuelerErziehungsberechtigte : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "erziehungsberechtigte",
	component: () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl),
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Erziehungsberechtigte"
	}
};
