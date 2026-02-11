import type { FoerderschwerpunkteDatenProps } from "~/components/schule/kataloge/foerderschwerpunkte/daten/FoerderschwerpunkteDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";

const FoerderschwerpunkteDaten = () => import("~/components/schule/kataloge/foerderschwerpunkte/daten/FoerderschwerpunkteDaten.vue");

export class RouteFoerderschwerpunkteDaten extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.foerderschwerpunkte.daten",
			"daten", FoerderschwerpunkteDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteDatenProps {
		return {
			manager: () => routeFoerderschwerpunkte.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeFoerderschwerpunkte.data.patch,
			schulform: api.schulform,
			schuljahr: api.abschnitt.schuljahr,
		};
	}
}

export const routeFoerderschwerpunkteDaten = new RouteFoerderschwerpunkteDaten();
