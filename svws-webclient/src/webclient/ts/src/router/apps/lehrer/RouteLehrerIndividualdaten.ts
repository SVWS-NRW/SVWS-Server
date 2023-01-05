import { RouteNode } from "~/router/RouteNode";
import { routeLehrer } from "~/router/apps/RouteLehrer";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<unknown> {

	public constructor() {
		super("lehrer_daten", "daten", SLehrerIndividualdaten);
		super.propHandler = (route) => routeLehrer.getProps(route);
		super.text = "Daten";
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

