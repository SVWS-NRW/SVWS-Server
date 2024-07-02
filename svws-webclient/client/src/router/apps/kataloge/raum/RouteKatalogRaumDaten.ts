import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { routeKatalogRaeume, type RouteKatalogRaeume } from "~/router/apps/kataloge/raum/RouteKatalogRaeume";

import type { RaumDatenProps } from "~/components/kataloge/raeume/daten/SRaumDatenProps";
import { routeApp } from "../../RouteApp";

const SRaumDaten = () => import("~/components/kataloge/raeume/daten/SRaumDaten.vue");

export class RouteKatalogRaumDaten extends RouteNode<any, RouteKatalogRaeume> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.raeume.daten", "daten", SRaumDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Raum";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogRaeume.data.raumListeManager.auswahlID() === null)
			return routeKatalogRaeume.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): RaumDatenProps {
		return {
			patch: routeKatalogRaeume.data.patch,
			auswahl: routeKatalogRaeume.data.raumListeManager.auswahl(),
		};
	}

}

export const routeKatalogRaumDaten = new RouteKatalogRaumDaten();

