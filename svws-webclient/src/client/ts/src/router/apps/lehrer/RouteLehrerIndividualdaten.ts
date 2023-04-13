import { RouteNode } from "~/router/RouteNode";
import type { RouteLehrer} from "~/router/apps/RouteLehrer";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeApp } from "~/router/RouteApp";
import type { LehrerIndividualdatenProps } from "~/components/lehrer/individualdaten/SLehrerIndividualdatenProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<unknown, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "lehrer.daten", "daten", SLehrerIndividualdaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
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
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile
		};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

