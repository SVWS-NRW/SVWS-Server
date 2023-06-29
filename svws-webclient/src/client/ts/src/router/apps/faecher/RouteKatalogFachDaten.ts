import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFaecher, type RouteKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";

import type { FachDatenProps } from "~/components/kataloge/faecher/daten/SFachDatenProps";

const SFachDaten = () => import("~/components/kataloge/faecher/daten/SFachDaten.vue");

export class RouteKatalogFachDaten extends RouteNode<unknown, RouteKatalogFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.faecher.daten", "daten", SFachDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogFaecher.data.auswahl === undefined)
			return routeKatalogFaecher.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): FachDatenProps {
		return {
			patch: routeKatalogFaecher.data.patch,
			data: routeKatalogFaecher.data.daten,
			mapKatalogeintraege: routeKatalogFaecher.data.mapKatalogeintraege
		};
	}

}

export const routeKatalogFachDaten = new RouteKatalogFachDaten();

