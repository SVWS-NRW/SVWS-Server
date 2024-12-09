import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleJahrgaenge, type RouteSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";

import type { JahrgangDatenProps } from "~/components/schule/jahrgaenge/daten/SJahrgangDatenProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SJahrgangDaten = () => import("~/components/schule/jahrgaenge/daten/SJahrgangDaten.vue");

export class RouteSchuleJahrgaengeDaten extends RouteNode<any, RouteSchuleJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.jahrgaenge.daten", "daten", SJahrgangDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang";
	}

	public getProps(to: RouteLocationNormalized): JahrgangDatenProps {
		return {
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			schulform: api.schulform,
			jahrgangListeManager: () => routeSchuleJahrgaenge.data.manager,
			patch: routeSchuleJahrgaenge.data.patch,
		};
	}

}

export const routeSchuleJahrgaengeDaten = new RouteSchuleJahrgaengeDaten();

