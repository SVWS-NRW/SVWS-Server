import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKurse, type RouteKurse } from "~/router/apps/kurse/RouteKurse";

import type { KursDatenProps } from "~/components/kurse/daten/SKursDatenProps";

const SKursDaten = () => import("~/components/kurse/daten/SKursDaten.vue");

export class RouteKursDaten extends RouteNode<unknown, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kurse.daten", "daten", SKursDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurs";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
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

