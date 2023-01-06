import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_kalender", "kalender", SGostKlausurplanungKalender);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Kalender";
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

