import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";

const SGostKlausurplanungPlanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanungPlanung.vue");

export class RouteGostKlausurplanungPlanung extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_planung", "planung", SGostKlausurplanungPlanung);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Detailplanung";
	}

}

export const routeGostKlausurplanungPlanung = new RouteGostKlausurplanungPlanung();

