import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { RouteLocationRaw } from "vue-router";

const SGostKlausurplanungPlanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanungPlanung.vue");

export class RouteGostKlausurplanungPlanung extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_planung", "planung", SGostKlausurplanungPlanung);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Detailplanung";
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

}

export const routeGostKlausurplanungPlanung = new RouteGostKlausurplanungPlanung();

