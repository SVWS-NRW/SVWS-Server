import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeJahrgaenge, type RouteJahrgaenge } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaenge";

import type { JahrgangDatenProps } from "~/components/schule/schulbezogen/jahrgaenge/daten/SJahrgaengeDatenProps";
import { api } from "~/router/Api";
import { routeApp } from "../../../RouteApp";

const SJahrgaengeDaten = () => import("~/components/schule/schulbezogen/jahrgaenge/daten/SJahrgaengeDaten.vue");

export class RouteJahrgaengeDaten extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.jahrgaenge.daten", "daten", SJahrgaengeDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang";
	}

	public getProps(to: RouteLocationNormalized): JahrgangDatenProps {
		return {
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			schulform: api.schulform,
			manager: () => routeJahrgaenge.data.manager,
			patch: routeJahrgaenge.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeJahrgaengeDaten = new RouteJahrgaengeDaten();

