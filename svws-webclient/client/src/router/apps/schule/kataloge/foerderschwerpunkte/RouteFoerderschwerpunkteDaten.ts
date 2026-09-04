import type { FoerderschwerpunkteDatenProps } from "~/components/schule/kataloge/foerderschwerpunkte/daten/FoerderschwerpunkteDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { RouteNode } from "~/router/RouteNode";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const FoerderschwerpunkteDaten = () => import("~/components/schule/kataloge/foerderschwerpunkte/daten/FoerderschwerpunkteDaten.vue");

export class RouteFoerderschwerpunkteDaten extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.foerderschwerpunkte.daten",
			"daten", FoerderschwerpunkteDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteDatenProps {
		return {
			manager: () => routeFoerderschwerpunkte.data.manager,
			patch: routeFoerderschwerpunkte.data.patch,
		};
	}
}

export const routeFoerderschwerpunkteDaten = new RouteFoerderschwerpunkteDaten();
