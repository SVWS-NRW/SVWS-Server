import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";

const SGostKlausurplanungKonflikte = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKonflikte.vue");

export class RouteGostKlausurplanungKonflikte extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_konflikte", "konflikte", SGostKlausurplanungKonflikte);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Konflikte";
	}

}

export const routeGostKlausurplanungKonflikte = new RouteGostKlausurplanungKonflikte();
