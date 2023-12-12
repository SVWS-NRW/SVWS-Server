import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogSchulen, type RouteKatalogSchulen } from "~/router/apps/kataloge/schulen/RouteKatalogSchulen";

import type { SchuleDatenProps } from "~/components/kataloge/schulen/daten/SSchuleDatenProps";

const SSchuleDaten = () => import("~/components/kataloge/schulen/daten/SSchuleDaten.vue");

export class RouteKatalogSchuleDaten extends RouteNode<unknown, RouteKatalogSchulen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.schulen.daten", "daten", SSchuleDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogSchulen.data.auswahl === undefined)
			return routeKatalogSchulen.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenProps {
		return {
			patch: routeKatalogSchulen.data.patch,
			mapKatalogeintraege: routeKatalogSchulen.data.mapKatalogeintraege
		};
	}

}

export const routeKatalogSchuleDaten = new RouteKatalogSchuleDaten();

