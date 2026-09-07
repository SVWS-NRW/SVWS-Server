import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { BetriebeDatenProps } from "~/components/schule/kataloge/betriebe/daten/BetriebeDatenProps";
import type { RouteBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { RouteNode } from "~/router/RouteNode";

const BetriebeDaten = () => import("~/components/schule/kataloge/betriebe/daten/BetriebeDaten.vue");

export class RouteBetriebeDaten extends RouteNode<any, RouteBetriebe> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.betriebe.daten", "daten", BetriebeDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betrieb";
	}

	public getProps(to: RouteLocationNormalized): BetriebeDatenProps {
		return {
			manager: () => routeBetriebe.data.manager,
			patch: routeBetriebe.data.patch,
			addAnsprechpartner: routeBetriebe.data.addAnsprechpartner,
			deleteAnsprechpartner: routeBetriebe.data.deleteAnsprechpartner,
			patchAnsprechpartner: routeBetriebe.data.patchAnsprechpartner,
		};
	}

}

export const routeBetriebeDaten = new RouteBetriebeDaten();

