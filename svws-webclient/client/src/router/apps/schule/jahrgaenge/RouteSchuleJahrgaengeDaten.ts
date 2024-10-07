import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleJahrgaenge, type RouteSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";

import type { JahrgangDatenProps } from "~/components/schule/jahrgaenge/daten/SJahrgangDatenProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SJahrgangDaten = () => import("~/components/schule/jahrgaenge/daten/SJahrgangDaten.vue");

export class RouteSchuleJahrgaengeDaten extends RouteNode<any, RouteSchuleJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.jahrgaenge.daten", "daten", SJahrgangDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeSchuleJahrgaenge.data.auswahl === undefined)
			return routeSchuleJahrgaenge.getRoute(undefined);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): JahrgangDatenProps {
		return {
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			schulform: api.schulform,
			patch: routeSchuleJahrgaenge.data.patch,
			data: () => routeSchuleJahrgaenge.data.daten,
			mapJahrgaenge: routeSchuleJahrgaenge.data.mapKatalogeintraege,
		};
	}

}

export const routeSchuleJahrgaengeDaten = new RouteSchuleJahrgaengeDaten();

