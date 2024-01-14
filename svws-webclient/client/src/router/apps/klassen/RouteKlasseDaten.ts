import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import type { KlassenDatenProps } from "~/components/klassen/daten/SKlassenDatenProps";

const SKlassenDaten = () => import("~/components/klassen/daten/SKlassenDaten.vue");

export class RouteKlasseDaten extends RouteNode<unknown, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen.daten", "daten", SKlassenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klasse";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): KlassenDatenProps {
		return {
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			patch: routeKlassen.data.patch,
			klassenListeManager: () => routeKlassen.data.klassenListeManager,
			mapKlassenVorigerAbschnitt: () => routeKlassen.data.mapKlassenVorigerAbschnitt,
			mapKlassenFolgenderAbschnitt: () => routeKlassen.data.mapKlassenFolgenderAbschnitt,
			gotoSchueler: routeKlassen.data.gotoSchueler,
			setzeDefaultSortierung: routeKlassen.data.setzeDefaultSortierung,
		};
	}

}

export const routeKlasseDaten = new RouteKlasseDaten();

