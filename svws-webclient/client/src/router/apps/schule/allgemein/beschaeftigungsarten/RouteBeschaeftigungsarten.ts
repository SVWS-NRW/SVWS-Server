import type { RouteParams } from "vue-router";
import type { BeschaeftigungsartenListeManager } from "@ui";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataBeschaeftigungsarten } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteDataBeschaeftigungsarten";
import { routeBeschaeftigungsartenDaten } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsartenDaten";
import { routeBeschaeftigungsartenNeu } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsartenNeu";
import { routeBeschaeftigungsartenGruppenprozesse } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsartenGruppenprozesse";

const SBeschaeftigungsartenApp = () => import("~/components/schule/allgemein/beschaeftigungsarten/SBeschaeftigungsartenApp.vue");
const SBeschaeftigungsartenAuswahl = () => import("~/components/schule/allgemein/beschaeftigungsarten/SBeschaeftigungsartenAuswahl.vue");

export class RouteBeschaeftigungsarten extends RouteAuswahlNode<BeschaeftigungsartenListeManager, RouteDataBeschaeftigungsarten, RouteApp> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.beschaeftigungsarten",
			"schule/beschaeftigungsarten/:id(\\d+)?", SBeschaeftigungsartenApp, SBeschaeftigungsartenAuswahl, new RouteDataBeschaeftigungsarten());
		super.mode = ServerMode.DEV;
		super.text = "Beschäftigungsarten";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeBeschaeftigungsartenDaten,
			routeBeschaeftigungsartenNeu,
			routeBeschaeftigungsartenGruppenprozesse,
		];
		super.defaultChild = routeBeschaeftigungsartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeBeschaeftigungsarten = new RouteBeschaeftigungsarten();
