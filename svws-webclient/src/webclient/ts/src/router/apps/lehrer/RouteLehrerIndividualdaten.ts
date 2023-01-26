import { RouteNode } from "~/router/RouteNode";
import { RouteLehrer, routeLehrer } from "~/router/apps/RouteLehrer";
import { RouteLocationRaw } from "vue-router";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<unknown, RouteLehrer> {

	public constructor() {
		super("lehrer_daten", "daten", SLehrerIndividualdaten);
		super.propHandler = (route) => routeLehrer.getProps(route);
		super.text = "Daten";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

