import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import type { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { routeGostKlausurplanung } from "../RouteGostKlausurplanung";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, GostKursklausurManager, Schulform, Vector } from "@core";

const SGostKlausurplanungPlanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanungPlanung.vue");

export class RouteGostKlausurplanungPlanung extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.planung", "planung", SGostKlausurplanungPlanung);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Detailplanung";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new Vector(), new Vector())},
			mapLehrer: routeGostKlausurplanung.data.mapLehrer,
			mapSchueler: routeGostKlausurplanung.data.mapSchueler,
			kursmanager: routeGostKlausurplanung.data.kursManager,
		}
	}

}

export const routeGostKlausurplanungPlanung = new RouteGostKlausurplanungPlanung();

