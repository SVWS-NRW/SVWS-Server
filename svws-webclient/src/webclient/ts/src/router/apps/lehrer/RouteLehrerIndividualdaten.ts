import { RouteNode } from "~/router/RouteNode";
import { RouteLehrer, routeLehrer } from "~/router/apps/RouteLehrer";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeApp } from "~/router/RouteApp";
import { LehrerIndividualdatenProps } from "~/components/lehrer/individualdaten/SLehrerIndividualdatenProps";
import { Schulform } from "@svws-nrw/svws-core-ts";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<unknown, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), "lehrer_daten", "daten", SLehrerIndividualdaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (routeLehrer.data.auswahl === undefined)
			return routeLehrer.getRoute(undefined);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): LehrerIndividualdatenProps {
		return {
			patch: routeLehrer.data.patchStammdaten,
			stammdaten: routeLehrer.data.stammdaten,
			orte: routeApp.data.orte,
			ortsteile: routeApp.data.ortsteile
		};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

