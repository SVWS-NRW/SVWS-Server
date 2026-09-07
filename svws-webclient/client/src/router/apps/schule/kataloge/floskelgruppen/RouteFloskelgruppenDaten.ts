import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { FloskelgruppenDatenProps } from "~/components/schule/kataloge/floskelgruppen/daten/FloskelgruppenDatenProps";
import type { RouteFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { routeFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { RouteNode } from "~/router/RouteNode";

const FloskelgruppenDaten = () => import("~/components/schule/kataloge/floskelgruppen/daten/FloskelgruppenDaten.vue");

export class RouteFloskelgruppenDaten extends RouteNode<any, RouteFloskelgruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskelgruppen.daten",
			"daten", FloskelgruppenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Floskelgruppe";
	}

	public getProps(to: RouteLocationNormalized): FloskelgruppenDatenProps {
		return {
			manager: () => routeFloskelgruppen.data.manager,
			patch: routeFloskelgruppen.data.patch,
		};
	}
}

export const routeFloskelgruppenDaten = new RouteFloskelgruppenDaten();
