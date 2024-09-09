import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

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
		if (routeKatalogFaecher.data.fachListeManager.auswahlID() === null)
			return routeKatalogFaecher.getRoute()
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): FachDatenProps {
		return {
			patch: routeKatalogFaecher.data.patch,
			fachListeManager: () => routeKatalogFaecher.data.fachListeManager,
		};
	}

}

export const routeKatalogFachDaten = new RouteKatalogFachDaten();

