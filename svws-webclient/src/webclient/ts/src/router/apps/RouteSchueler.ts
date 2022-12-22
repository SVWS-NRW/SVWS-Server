import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { ref } from "vue";
import { RouteLocationNormalizedLoaded, RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
import { routeAppData, routeAppMeta, RouteAppMeta, routeAuswahlID, routePropsAuswahlID } from "~/router/RouteUtils";
import { RouteSchuelerAbschnitt } from "./schueler/RouteSchuelerAbschnitt";
import { RouteSchuelerAdressen } from "./schueler/RouteSchuelerAdressen";
import { RouteSchuelerErziehungsberechtigte } from "./schueler/RouteSchuelerErziehungsberechtigte";
import { RouteSchuelerIndividualdaten } from "./schueler/RouteSchuelerIndividualdaten";
import { RouteSchuelerLaufbahnplanung } from "./schueler/RouteSchuelerLaufbahnplanung";
import { RouteSchuelerLeistungsdaten } from "./schueler/RouteSchuelerLeistungsdaten";
import { RouteSchuelerSchulbesuch } from "./schueler/RouteSchuelerSchulbesuch";
import { RouteSchuelerStundenplan } from "./schueler/RouteSchuelerStundenplan";

const ROUTE_NAME: string = "schueler";

export const RouteSchuelerChildren: RouteRecordRaw[] = [
	RouteSchuelerIndividualdaten,
	RouteSchuelerErziehungsberechtigte,
	RouteSchuelerAdressen,
	RouteSchuelerSchulbesuch,
	RouteSchuelerAbschnitt,
	RouteSchuelerLeistungsdaten,
	RouteSchuelerLaufbahnplanung,
	RouteSchuelerStundenplan
];


export interface RouteDataSchueler {
	item: SchuelerListeEintrag | undefined;
	stammdaten: DataSchuelerStammdaten;
}

export const RouteSchueler : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schueler/:id(\\d+)?",
	components: {
		default: () => import("~/components/schueler/SSchuelerApp.vue"),
		liste: () => import("~/components/schueler/SSchuelerAuswahl.vue")
	},
	props: {
		default: (route) => {
			const prop = routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl);
			const data: RouteDataSchueler = routeAppData(RouteSchueler);
			if (prop.item !== data.item) {
				if (prop.item === undefined) {
					data.item = undefined;
					data.stammdaten.unselect();
				} else {
					data.item = prop.item as SchuelerListeEintrag;
					data.stammdaten.select(data.item);
				}
			}
			return prop;
		},
		liste: (route) => routePropsAuswahlID(route, injectMainApp().apps.schueler.auswahl)
	},
	meta: <RouteAppMeta<SchuelerListeEintrag | undefined, unknown>> {
		auswahl: () => routeAuswahlID(ROUTE_NAME, injectMainApp().apps.schueler.auswahl),
		hidden: () => false,
		redirect: ref(RouteSchuelerIndividualdaten),
		data: <RouteDataSchueler>{
			item: undefined,
			stammdaten: new DataSchuelerStammdaten()
		}
	},
	redirect: to => {
		return to.path + "/" + routeAppMeta(RouteSchueler)?.redirect.value.path;
	},
	children: RouteSchuelerChildren
}


export function routeSchuelerSetRedirect(route : RouteLocationNormalizedLoaded) {
	const meta = RouteSchueler.meta as RouteAppMeta<SchuelerListeEintrag | undefined, RouteDataSchueler>;
	if ((RouteSchueler.children === undefined) || (meta === undefined))
		return;
	for (var child of RouteSchueler.children) {
		if (route.name === child.name) {
			meta.redirect.value = child;
			return;
		}
	}
	meta.redirect.value = RouteSchuelerIndividualdaten;
}
