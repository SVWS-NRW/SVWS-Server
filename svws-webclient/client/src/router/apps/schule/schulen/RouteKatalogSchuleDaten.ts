import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogSchulen, type RouteKatalogSchulen } from "~/router/apps/schule/schulen/RouteKatalogSchulen";

import type { SchuleDatenProps } from "~/components/schule/kataloge/schulen/daten/SSchuleDatenProps";
import { api } from "~/router/Api";

const SSchuleDaten = () => import("~/components/schule/kataloge/schulen/daten/SSchuleDaten.vue");

export class RouteKatalogSchuleDaten extends RouteNode<any, RouteKatalogSchulen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.schulen.daten", "daten", SSchuleDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogSchulen.data.auswahl === undefined)
			return routeKatalogSchulen.getRoute(undefined);
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenProps {
		return {
			schuljahr: api.abschnitt.schuljahr,
			patch: routeKatalogSchulen.data.patch,
			mapKatalogeintraege: routeKatalogSchulen.data.mapKatalogeintraege,
			auswahl: routeKatalogSchulen.data.auswahl,
		};
	}

}

export const routeKatalogSchuleDaten = new RouteKatalogSchuleDaten();

