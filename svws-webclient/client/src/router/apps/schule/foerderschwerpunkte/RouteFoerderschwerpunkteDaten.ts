import type { FoerderschwerpunkteDatenProps } from "~/components/schule/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunkteDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/foerderschwerpunkte/RouteFoerderschwerpunkte";

const SFoerderschwerpunkteDaten = () => import("~/components/schule/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunkteDaten.vue")

export class RouteFoerderschwerpunkteDaten extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.foerderschwerpunkte.daten",
			"daten", SFoerderschwerpunkteDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte"
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteDatenProps {
		return {
			manager: () => routeFoerderschwerpunkte.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeFoerderschwerpunkte.data.patch,
		}
	}
}

export const routeFoerderschwerpunkteDaten = new RouteFoerderschwerpunkteDaten();
