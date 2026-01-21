import { RouteNode } from "~/router/RouteNode";
import { routeBetriebsarten, type RouteBetriebsarten } from "./RouteBetriebsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized } from "vue-router";
import type { BetriebsartenDatenProps } from "~/components/schule/kataloge/betriebsarten/daten/BetriebsartenDatenProps";
import { api } from "~/router/Api";

const BetriebsartenDaten = () => import("~/components/schule/kataloge/betriebsarten/daten/BetriebsartenDaten.vue");


class RouteBetriebsartenDaten extends RouteNode<any, RouteBetriebsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.betriebsarten.daten",
			"daten", BetriebsartenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betriebsarten";
	}

	public getProps(to: RouteLocationNormalized): BetriebsartenDatenProps {
		return {
			patch: routeBetriebsarten.data.patch,
			manager: () => routeBetriebsarten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeBetriebsartenDaten = new RouteBetriebsartenDaten();
