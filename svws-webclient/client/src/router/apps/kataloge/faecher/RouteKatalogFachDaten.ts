import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFaecher, type RouteKatalogFaecher } from "~/router/apps/kataloge/faecher/RouteKatalogFaecher";

import type { FachDatenProps } from "~/components/kataloge/faecher/daten/SFachDatenProps";
import { routeApp } from "../../RouteApp";

const SFachDaten = () => import("~/components/kataloge/faecher/daten/SFachDaten.vue");

export class RouteKatalogFachDaten extends RouteNode<any, RouteKatalogFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.faecher.daten", "daten", SFachDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogFaecher.data.auswahl === undefined)
			return routeKatalogFaecher.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		api.schulform
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): FachDatenProps {
		return {
			schulform: api.schulform,
			patch: routeKatalogFaecher.data.patch,
			data: () => routeKatalogFaecher.data.daten,
			mapKatalogeintraege: () => routeKatalogFaecher.data.mapKatalogeintraege
		};
	}

}

export const routeKatalogFachDaten = new RouteKatalogFachDaten();

