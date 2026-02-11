import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import type { RouteFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { routeFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import type { FloskelgruppenDatenProps } from "~/components/schule/kataloge/floskelgruppen/daten/FloskelgruppenDatenProps";

const FloskelgruppenDaten = () => import("~/components/schule/kataloge/floskelgruppen/daten/FloskelgruppenDaten.vue");

export class RouteFloskelgruppenDaten extends RouteNode<any, RouteFloskelgruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskelgruppen.daten",
			"daten", FloskelgruppenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Floskelgruppe";
	}

	public getProps(to: RouteLocationNormalized): FloskelgruppenDatenProps {
		return {
			manager: () => routeFloskelgruppen.data.manager,
			schuljahr: api.abschnitt.schuljahr,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeFloskelgruppen.data.patch,
		};
	}
}

export const routeFloskelgruppenDaten = new RouteFloskelgruppenDaten();
