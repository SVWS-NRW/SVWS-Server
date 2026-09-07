import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { SchwerpunkteDatenProps } from "~/components/schule/kataloge/schwerpunkte/daten/SchwerpunkteDatenProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteSchwerpunkte, routeSchwerpunkte } from "./RouteSchwerpunkte";

const SchwerpunkteDaten = () => import("~/components/schule/kataloge/schwerpunkte/daten/SchwerpunkteDaten.vue");

class RouteSchwerpunkteDaten extends RouteNode<any, RouteSchwerpunkte> {
	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB, Schulform.R], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schwerpunkte.daten",
			"daten", SchwerpunkteDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schwerpunkte";
	}

	public getProps(to: RouteLocationNormalized): SchwerpunkteDatenProps {
		return {
			patch: routeSchwerpunkte.data.patch,
			manager: () => routeSchwerpunkte.data.manager,
		};
	}
}

export const routeSchwerpunkteDaten = new RouteSchwerpunkteDaten();
