import { RouteNode } from "~/router/RouteNode";
import { RouteLehrer, routeLehrer } from "~/router/apps/RouteLehrer";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { routeApp } from "~/router/RouteApp";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<unknown, RouteLehrer> {

	public constructor() {
		super("lehrer_daten", "daten", SLehrerIndividualdaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			stammdaten: routeLehrer.data.stammdaten,
			orte: routeApp.data.orte,
			ortsteile: routeApp.data.ortsteile
		};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

