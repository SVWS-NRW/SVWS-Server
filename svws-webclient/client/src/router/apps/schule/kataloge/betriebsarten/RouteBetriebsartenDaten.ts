import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { BetriebsartenDatenProps } from "~/components/schule/kataloge/betriebsarten/daten/BetriebsartenDatenProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteBetriebsarten, routeBetriebsarten } from "./RouteBetriebsarten";

const BetriebsartenDaten = () => import("~/components/schule/kataloge/betriebsarten/daten/BetriebsartenDaten.vue");


class RouteBetriebsartenDaten extends RouteNode<any, RouteBetriebsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.betriebsarten.daten",
			"daten", BetriebsartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betriebsarten";
	}

	public getProps(to: RouteLocationNormalized): BetriebsartenDatenProps {
		return {
			patch: routeBetriebsarten.data.patch,
			manager: () => routeBetriebsarten.data.manager,
		};
	}
}

export const routeBetriebsartenDaten = new RouteBetriebsartenDaten();
