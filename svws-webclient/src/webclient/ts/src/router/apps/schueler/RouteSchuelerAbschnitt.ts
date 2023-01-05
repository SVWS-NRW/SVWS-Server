import { RouteNode } from "~/router/RouteNode";
import { routeSchueler } from "../RouteSchueler";


const SSchuelerAbschnitt = () => import("~/components/schueler/abschnitt/SSchuelerAbschnitt.vue");

export class RouteSchuelerAbschnitt extends RouteNode<unknown> {

	public constructor() {
		super("schueler_abschnitt", "abschnitt", SSchuelerAbschnitt);
		super.propHandler = (route) => routeSchueler.getProps(route);
		super.text = "Aktueller Abschnitt";
	}

}

export const routeSchuelerAbschnitt = new RouteSchuelerAbschnitt();

