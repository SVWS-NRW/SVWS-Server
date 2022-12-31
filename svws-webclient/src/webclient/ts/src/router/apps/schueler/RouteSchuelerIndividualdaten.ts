import { RouteNode } from "~/router/RouteNode";
import { routeSchueler } from "~/router/apps/RouteSchueler";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdaten.vue");

export class RouteSchuelerIndividualdaten extends RouteNode<unknown> {

	public constructor() {
		super("schueler_daten", "daten", SSchuelerIndividualdaten);
		super.propHandler = (route) => routeSchueler.getProps(route);
		super.text = "Individualdaten";
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

