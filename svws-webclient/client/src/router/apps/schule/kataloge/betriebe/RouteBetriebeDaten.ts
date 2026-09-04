import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import type { BetriebeDatenProps } from "~/components/schule/kataloge/betriebe/daten/BetriebeDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

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

