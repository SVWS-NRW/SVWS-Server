import { RouteNode } from "~/router/RouteNode";
import { routeSchwerpunkte, type RouteSchwerpunkte } from "./RouteSchwerpunkte";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized } from "vue-router";
import type { SchwerpunkteDatenProps } from "~/components/schule/kataloge/schwerpunkte/daten/SchwerpunkteDatenProps";

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
