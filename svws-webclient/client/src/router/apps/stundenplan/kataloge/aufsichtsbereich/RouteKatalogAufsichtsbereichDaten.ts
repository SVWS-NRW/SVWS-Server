import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { routeKatalogAufsichtsbereiche, type RouteKatalogAufsichtsbereiche } from "~/router/apps/stundenplan/kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereiche";

import type { AufsichtsbereichDatenProps } from "~/components/stundenplan/kataloge/aufsichtsbereiche/daten/SAufsichtsbereichDatenProps";
import { routeApp } from "../../../RouteApp";

const SAufsichtsbereichDaten = () => import("~/components/stundenplan/kataloge/aufsichtsbereiche/daten/SAufsichtsbereichDaten.vue");

export class RouteKatalogAufsichtsbereichDaten extends RouteNode<any, RouteKatalogAufsichtsbereiche> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.aufsichtsbereiche.daten", "daten", SAufsichtsbereichDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Aufsichtsbereich";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogAufsichtsbereiche.data.auswahl === undefined)
			return routeKatalogAufsichtsbereiche.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id }};
	}

	public getProps(to: RouteLocationNormalized): AufsichtsbereichDatenProps {
		return {
			patch: routeKatalogAufsichtsbereiche.data.patch,
			auswahl: routeKatalogAufsichtsbereiche.data.auswahl,
		};
	}

}

export const routeKatalogAufsichtsbereichDaten = new RouteKatalogAufsichtsbereichDaten();

