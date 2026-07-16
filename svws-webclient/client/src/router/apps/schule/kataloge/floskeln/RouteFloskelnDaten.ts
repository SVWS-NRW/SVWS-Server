import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import { routeFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import type { FloskelnDatenProps } from "~/components/schule/kataloge/floskeln/daten/FloskelnDatenProps";

const FloskelnDaten = () => import("~/components/schule/kataloge/floskeln/daten/FloskelnDaten.vue");

export class RouteFloskelnDaten extends RouteNode<any, RouteFloskeln> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskeln.daten",
			"daten", FloskelnDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Floskelgruppe";
	}

	public getProps(to: RouteLocationNormalized): FloskelnDatenProps {
		return {
			manager: () => routeFloskeln.data.manager,
			patch: routeFloskeln.data.patch,
		};
	}
}

export const routeFloskelnDaten = new RouteFloskelnDaten();
