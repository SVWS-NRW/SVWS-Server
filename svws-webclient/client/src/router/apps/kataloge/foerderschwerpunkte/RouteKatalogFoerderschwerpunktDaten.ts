import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFoerderschwerpunkte, type RouteKatalogFoerderschwerpunkte } from "~/router/apps/kataloge/foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeApp } from "../../RouteApp";

const SFoerderschwerpunktDaten = () => import("~/components/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunktDaten.vue");

export class RouteKatalogFoerderschwerpunktDaten extends RouteNode<unknown, RouteKatalogFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.foerderschwerpunkte.daten", "daten", SFoerderschwerpunktDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkt";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogFoerderschwerpunkte.data.auswahl === undefined)
			return routeKatalogFoerderschwerpunkte.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: routeKatalogFoerderschwerpunkte.data.patch,
			data: routeKatalogFoerderschwerpunkte.data.daten,
			mapKatalogeintraege: routeKatalogFoerderschwerpunkte.data.mapKatalogeintraege
		};
	}

}

export const routeKatalogFoerderschwerpunktDaten = new RouteKatalogFoerderschwerpunktDaten();

