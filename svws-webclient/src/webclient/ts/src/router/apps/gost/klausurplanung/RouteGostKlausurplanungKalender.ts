import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { RouteLocationRaw } from "vue-router";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_kalender", "kalender", SGostKlausurplanungKalender);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Kalender";
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

