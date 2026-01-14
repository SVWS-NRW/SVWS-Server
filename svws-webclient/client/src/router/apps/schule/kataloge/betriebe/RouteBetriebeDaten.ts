import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import type { RouteBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import type { BetriebeDatenProps } from "~/components/schule/kataloge/betriebe/daten/BetriebeDatenProps";

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
			benutzerKompetenzen: api.benutzerKompetenzen,
			addAnsprechpartner: routeBetriebe.data.addAnsprechpartner,
			deleteAnsprechpartner: routeBetriebe.data.deleteAnsprechpartner,
			patchAnsprechpartner: routeBetriebe.data.patchAnsprechpartner,
		};
	}

}

export const routeBetriebeDaten = new RouteBetriebeDaten();

