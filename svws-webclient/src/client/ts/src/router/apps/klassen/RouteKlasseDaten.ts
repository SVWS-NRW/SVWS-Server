import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "../RouteKlassen";

import type { KlassenDatenProps } from "~/components/klassen/daten/SKlassenDatenProps";

const SKlassenDaten = () => import("~/components/klassen/daten/SKlassenDaten.vue");

export class RouteKlasseDaten extends RouteNode<unknown, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen.daten", "daten", SKlassenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klasse";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKlassen.data.auswahl === undefined)
			return routeKlassen.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): KlassenDatenProps {
		return {
			patch: routeKlassen.data.patch,
			data: routeKlassen.data.daten,
			mapLehrer: routeKlassen.data.mapLehrer,
			mapJahrgaenge: routeKlassen.data.mapJahrgaenge,
			gotoSchueler: routeKlassen.data.gotoSchueler,
		};
	}

}

export const routeKlasseDaten = new RouteKlasseDaten();

