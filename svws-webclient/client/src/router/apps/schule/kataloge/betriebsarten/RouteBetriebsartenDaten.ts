import { RouteNode } from "~/router/RouteNode";
import { routeBetriebsarten, type RouteBetriebsarten } from "./RouteBetriebsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized } from "vue-router";
import type { BetriebsartenDatenProps } from "~/components/schule/kataloge/betriebsarten/daten/BetriebsartenDatenProps";

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
