import type { BeschaeftigungsartenDatenProps } from "~/components/schule/kataloge/beschaeftigungsarten/daten/BeschaeftigungsartenDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { RouteNode } from "~/router/RouteNode";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const BeschaeftigungsartenDaten = () => import("~/components/schule/kataloge/beschaeftigungsarten/daten/BeschaeftigungsartenDaten.vue");

export class RouteBeschaeftigungsartenDaten extends RouteNode<any, RouteBeschaeftigungsarten> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.beschaeftigungsarten.daten",
			"daten", BeschaeftigungsartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Beschäftigungsart";
	}

	public getProps(to: RouteLocationNormalized): BeschaeftigungsartenDatenProps {
		return {
			manager: () => routeBeschaeftigungsarten.data.manager,
			patch: routeBeschaeftigungsarten.data.patch,
		};
	}
}

export const routeBeschaeftigungsartenDaten = new RouteBeschaeftigungsartenDaten();
