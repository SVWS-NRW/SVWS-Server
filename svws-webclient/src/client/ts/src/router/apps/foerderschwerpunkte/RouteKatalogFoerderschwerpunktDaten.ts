import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";
import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";
import { RouteNode } from "~/router/RouteNode";

const SFoerderschwerpunktDaten = () => import("~/components/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunktDaten.vue");

export class RouteKatalogFoerderschwerpunktDaten extends RouteNode<unknown, RouteKatalogFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.foerderschwerpunkte.daten", "daten", SFoerderschwerpunktDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkt";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKatalogFoerderschwerpunkte.data.auswahl === undefined)
			return routeKatalogFoerderschwerpunkte.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
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

