import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeJahrgaenge, type RouteJahrgaenge } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaenge";
import type { JahrgaengeDatenProps } from "~/components/schule/kataloge/jahrgaenge/daten/JahrgaengeDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

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
