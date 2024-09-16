import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { routeKatalogPausenzeiten, type RouteKatalogPausenzeiten } from "~/router/apps/stundenplan/kataloge/pausenzeit/RouteKatalogPausenzeiten";

import type { PausenzeitDatenProps } from "~/components/stundenplan/kataloge/pausenzeiten/daten/SPausenzeitDatenProps";
import { routeApp } from "../../../RouteApp";

const SPausenzeitDaten = () => import("~/components/stundenplan/kataloge/pausenzeiten/daten/SPausenzeitDaten.vue");

export class RouteKatalogPausenzeitDaten extends RouteNode<any, RouteKatalogPausenzeiten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.pausenzeiten.daten", "daten", SPausenzeitDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausenzeit";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogPausenzeiten.data.auswahl === undefined)
			return routeKatalogPausenzeiten.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id }};
	}

	public getProps(to: RouteLocationNormalized): PausenzeitDatenProps {
		return {
			patch: routeKatalogPausenzeiten.data.patch,
			auswahl: routeKatalogPausenzeiten.data.auswahl,
		};
	}

}

export const routeKatalogPausenzeitDaten = new RouteKatalogPausenzeitDaten();

