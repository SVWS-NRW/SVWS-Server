import type { RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { BeschaeftigungsartenListeManager } from "@ui/ui/manager/kataloge/BeschaeftigungsartenListeManager";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeBeschaeftigungsartenDaten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsartenDaten";
import { routeBeschaeftigungsartenGruppenprozesse } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsartenGruppenprozesse";
import { routeBeschaeftigungsartenNeu } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsartenNeu";
import { RouteDataBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteDataBeschaeftigungsarten";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";

const BeschaeftigungsartenApp = () => import("~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenApp.vue");
const BeschaeftigungsartenAuswahl = () => import("~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenAuswahl.vue");

export class RouteBeschaeftigungsarten extends RouteAuswahlNode<BeschaeftigungsartenListeManager, RouteDataBeschaeftigungsarten, RouteApp> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.beschaeftigungsarten",
			"schule/beschaeftigungsarten/:id(\\d+)?", BeschaeftigungsartenApp, BeschaeftigungsartenAuswahl, new RouteDataBeschaeftigungsarten());
		super.mode = ServerMode.STABLE;
		super.text = "Beschäftigungsarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeBeschaeftigungsartenDaten,
			routeBeschaeftigungsartenNeu,
			routeBeschaeftigungsartenGruppenprozesse,
		];
		super.defaultChild = routeBeschaeftigungsartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeBeschaeftigungsarten = new RouteBeschaeftigungsarten();
