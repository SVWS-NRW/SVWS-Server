import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "../../RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";

const SGostKlausurplanungDaten = () => import("~/components/gost/klausurplanung/SGostKlausurplanungDaten.vue");

export class RouteGostKlausurplanungKlausurdaten extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost_klausurplanung_klausurdaten", "klausurdaten", SGostKlausurplanungDaten);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Klausurdaten";
	}

}

export const routeGostKlausurplanungKlausurdaten = new RouteGostKlausurplanungKlausurdaten();

