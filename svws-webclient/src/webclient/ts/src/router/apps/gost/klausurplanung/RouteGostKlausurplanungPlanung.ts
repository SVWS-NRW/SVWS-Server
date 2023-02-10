import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

const SGostKlausurplanungPlanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanungPlanung.vue");

export class RouteGostKlausurplanungPlanung extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_planung", "planung", SGostKlausurplanungPlanung);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Detailplanung";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			jahrgangsdaten: routeGost.data.jahrgangsdaten
		}
	}

}

export const routeGostKlausurplanungPlanung = new RouteGostKlausurplanungPlanung();

