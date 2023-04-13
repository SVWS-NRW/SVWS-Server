import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { KursDatenProps } from "~/components/kurse/daten/SKursDatenProps";
import type { RouteKurse } from "~/router/apps/RouteKurse";
import { routeKurse } from "~/router/apps/RouteKurse";
import { RouteNode } from "~/router/RouteNode";

const SKursDaten = () => import("~/components/kurse/daten/SKursDaten.vue");

export class RouteKursDaten extends RouteNode<unknown, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kurse.daten", "daten", SKursDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKurse.data.auswahl === undefined)
			return routeKurse.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): KursDatenProps {
		return {
			patch: routeKurse.data.patch,
			mapJahrgaenge: routeKurse.data.mapJahrgaenge,
			mapLehrer: routeKurse.data.mapLehrer,
			data: routeKurse.data.daten,
			gotoSchueler: routeKurse.data.gotoSchueler,
		};
	}

}

export const routeKursDaten = new RouteKursDaten();

