import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFoerderschwerpunkte, type RouteKatalogFoerderschwerpunkte } from "~/router/apps/schule/foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeApp } from "~/router/apps/RouteApp";

const SFoerderschwerpunktDaten = () => import("~/components/schule/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunktDaten.vue");

export class RouteKatalogFoerderschwerpunktDaten extends RouteNode<any, RouteKatalogFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.foerderschwerpunkte.daten", "daten", SFoerderschwerpunktDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkt";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogFoerderschwerpunkte.data.auswahl === undefined)
			return routeKatalogFoerderschwerpunkte.getRoute();
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			patch: routeKatalogFoerderschwerpunkte.data.patch,
			data: routeKatalogFoerderschwerpunkte.data.daten,
			mapKatalogeintraege: routeKatalogFoerderschwerpunkte.data.mapKatalogeintraege,
		};
	}

}

export const routeKatalogFoerderschwerpunktDaten = new RouteKatalogFoerderschwerpunktDaten();

