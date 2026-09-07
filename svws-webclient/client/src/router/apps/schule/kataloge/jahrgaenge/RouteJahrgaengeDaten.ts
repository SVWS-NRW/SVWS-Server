import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { JahrgaengeDatenProps } from "~/components/schule/kataloge/jahrgaenge/daten/JahrgaengeDatenProps";
import { type RouteJahrgaenge, routeJahrgaenge } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaenge";
import { RouteNode } from "~/router/RouteNode";

const JahrgaengeDaten = () => import("~/components/schule/kataloge/jahrgaenge/daten/JahrgaengeDaten.vue");

export class RouteJahrgaengeDaten extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.jahrgaenge.daten", "daten", JahrgaengeDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang";
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeDatenProps {
		return {
			manager: () => routeJahrgaenge.data.manager,
			patch: routeJahrgaenge.data.patch,
		};
	}

}

export const routeJahrgaengeDaten = new RouteJahrgaengeDaten();
