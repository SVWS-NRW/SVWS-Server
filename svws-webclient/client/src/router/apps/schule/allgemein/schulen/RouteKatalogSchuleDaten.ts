import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogSchulen, type RouteKatalogSchulen } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchulen";
import type { SchuleDatenProps } from "~/components/schule/allgemein/schulen/daten/SSchuleDatenProps";
import { api } from "~/router/Api";

const SSchuleDaten = () => import("~/components/schule/allgemein/schulen/daten/SSchuleDaten.vue");

export class RouteKatalogSchuleDaten extends RouteNode<any, RouteKatalogSchulen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.schulen.daten", "daten", SSchuleDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenProps {
		return {
			schuljahr: api.abschnitt.schuljahr,
			schuleListeManager: () => routeKatalogSchulen.data.manager,
			patch: routeKatalogSchulen.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeKatalogSchuleDaten = new RouteKatalogSchuleDaten();

