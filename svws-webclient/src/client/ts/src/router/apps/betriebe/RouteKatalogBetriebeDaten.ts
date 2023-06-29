import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogBetriebe, type RouteKatalogBetriebe } from "~/router/apps/RouteKatalogBetriebe";

import type { BetriebeDatenProps } from "~/components/kataloge/betriebe/daten/SBetriebeProps";



const SBetriebeDaten = () => import("~/components/kataloge/betriebe/daten/SBetriebedaten.vue")

export class RouteKatalogBetriebeDaten extends RouteNode<unknown, RouteKatalogBetriebe> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.betriebe.daten", "daten", SBetriebeDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betrieb";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogBetriebe.data.auswahl === undefined)
			return routeKatalogBetriebe.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): BetriebeDatenProps {
		return {
			patch: routeKatalogBetriebe.data.patch,
			patchBetriebAnpsrechpartner: routeKatalogBetriebe.data.patchBetriebAnsprechpartner,
			addBetriebAnsprechpartner: routeKatalogBetriebe.data.addBetriebAnsprechpartner,
			removeBetriebAnsprechpartner: routeKatalogBetriebe.data.removeBetriebAnsprechpartner,
			data: routeKatalogBetriebe.data.daten,
			mapBeschaeftigungsarten: routeKatalogBetriebe.data.mapBeschaeftigungsarten,
			mapOrte: routeApp.data.mapOrte,
			mapAnsprechpartner: routeKatalogBetriebe.data.mapAnsprechpartner,

		};
	}

}

export const routeKatalogBetriebeDaten = new RouteKatalogBetriebeDaten();

