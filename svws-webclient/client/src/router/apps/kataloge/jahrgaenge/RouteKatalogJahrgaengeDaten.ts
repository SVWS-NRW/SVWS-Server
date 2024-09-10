import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogJahrgaenge, type RouteKatalogJahrgaenge } from "~/router/apps/kataloge/jahrgaenge/RouteKatalogJahrgaenge";

import type { JahrgangDatenProps } from "~/components/kataloge/jahrgaenge/daten/SJahrgangDatenProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SJahrgangDaten = () => import("~/components/kataloge/jahrgaenge/daten/SJahrgangDaten.vue");

export class RouteKatalogJahrgaengeDaten extends RouteNode<any, RouteKatalogJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.jahrgaenge.daten", "daten", SJahrgangDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogJahrgaenge.data.auswahl === undefined)
			return routeKatalogJahrgaenge.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): JahrgangDatenProps {
		return {
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			schulform: api.schulform,
			patch: routeKatalogJahrgaenge.data.patch,
			data: () => routeKatalogJahrgaenge.data.daten,
			mapJahrgaenge: routeKatalogJahrgaenge.data.mapKatalogeintraege
		};
	}

}

export const routeKatalogJahrgaengeDaten = new RouteKatalogJahrgaengeDaten();

