import type { BeschaeftigungsartenDatenProps } from "~/components/schule/kataloge/beschaeftigungsarten/daten/SBeschaeftigungsartenDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteBeschaeftigungsarten } from "~/router/apps/schule/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/beschaeftigungsarten/RouteBeschaeftigungsarten";

const SBeschaeftigungsartenDaten = () => import("~/components/schule/kataloge/beschaeftigungsarten/daten/SBeschaeftigungsartenDaten.vue")

export class RouteBeschaeftigungsartenDaten extends RouteNode<any, RouteBeschaeftigungsarten> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.beschaeftigungsarten.daten",
			"daten", SBeschaeftigungsartenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Beschäftigungsart";
	}

	public getProps(to: RouteLocationNormalized): BeschaeftigungsartenDatenProps {
		return {
			manager: () => routeBeschaeftigungsarten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeBeschaeftigungsarten.data.patch,
		}
	}
}

export const routeBeschaeftigungsartenDaten = new RouteBeschaeftigungsartenDaten();
