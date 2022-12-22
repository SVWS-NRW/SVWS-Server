import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteRecordRaw } from "vue-router";
import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";
import { injectMainApp } from "~/apps/Main";
import { routeAppData, RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "lehrer_personaldaten";

export interface RouteDataLehrerPersonaldaten {
	item: LehrerListeEintrag | undefined;
	personaldaten: DataLehrerPersonaldaten;
}

export const RouteLehrerPersonaldaten : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "personaldaten",
	component: () => import("~/components/lehrer/personaldaten/SLehrerPersonaldaten.vue"),
	props: (route) => {
		const prop = routePropsAuswahlID(route, injectMainApp().apps.lehrer.auswahl);
		const data: RouteDataLehrerPersonaldaten = routeAppData(RouteLehrerPersonaldaten);
		if (prop.item !== data.item) {
			if (prop.item === undefined) {
				data.item = undefined;
				data.personaldaten.unselect();
			} else {
				data.item = prop.item as LehrerListeEintrag;
				data.personaldaten.select(data.item);
			}
		}
		return prop;
	},
	meta: <RouteAppMeta<unknown, RouteDataLehrerPersonaldaten>> {
		auswahl: () => {},
		hidden: () => false,
		text: "Personaldaten",
		data: <RouteDataLehrerPersonaldaten>{
			item: undefined,
			personaldaten: new DataLehrerPersonaldaten()
		}
	}
};
