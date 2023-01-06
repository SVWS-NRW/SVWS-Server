import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausurplanung/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_schienen", "schienen", SGostKlausurplanungSchienen);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Schienen";
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();


