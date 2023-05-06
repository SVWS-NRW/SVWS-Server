import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import type { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { routeGostKlausurplanung } from "../RouteGostKlausurplanung";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, GostKlausurvorgabenManager, GostKursklausurManager, Schulform, ArrayList } from "@svws-nrw/svws-core";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.kalender", "kalender", SGostKlausurplanungKalender);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalender";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new ArrayList(), new ArrayList())},
			mapLehrer: routeGostKlausurplanung.data.mapLehrer,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			kursmanager: routeGostKlausurplanung.data.kursManager,
		}
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

