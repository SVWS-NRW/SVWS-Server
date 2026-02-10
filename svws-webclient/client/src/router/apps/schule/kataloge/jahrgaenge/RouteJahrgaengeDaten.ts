import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeJahrgaenge, type RouteJahrgaenge } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaenge";

import type { JahrgaengeDatenProps } from "~/components/schule/kataloge/jahrgaenge/daten/JahrgaengeDatenProps";
import { api } from "~/router/Api";

const JahrgaengeDaten = () => import("~/components/schule/kataloge/jahrgaenge/daten/JahrgaengeDaten.vue");

export class RouteJahrgaengeDaten extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.jahrgaenge.daten", "daten", JahrgaengeDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang";
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeDatenProps {
		return {
			schuljahr: api.abschnitt.schuljahr,
			schulform: api.schulform,
			manager: () => routeJahrgaenge.data.manager,
			patch: routeJahrgaenge.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeJahrgaengeDaten = new RouteJahrgaengeDaten();

