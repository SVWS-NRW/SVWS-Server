import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { RouteLocationRaw } from "vue-router";

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausurplanung/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_schienen", "schienen", SGostKlausurplanungSchienen);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Schienen";
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();


