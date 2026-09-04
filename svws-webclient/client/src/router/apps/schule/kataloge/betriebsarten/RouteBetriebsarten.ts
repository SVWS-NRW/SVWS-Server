import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteParams } from "vue-router";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeBetriebsartenNeu } from "./RouteBetriebsartenNeu";
import { routeBetriebsartenDaten } from "./RouteBetriebsartenDaten";
import { RouteDataBetriebsarten } from "./RouteDataBetriebsarten";
import { routeBetriebsartenGruppenprozesse } from "./RouteBetriebsartenGruppenprozesse";
import BetriebsartenApp from "~/components/schule/kataloge/betriebsarten/BetriebsartenApp.vue";
import BetriebsartenAuswahl from "~/components/schule/kataloge/betriebsarten/BetriebsartenAuswahl.vue";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { BetriebsartenListeManager } from "@ui/ui/manager/kataloge/BetriebsartenListeManager";

export class RouteBetriebsarten extends RouteAuswahlNode<BetriebsartenListeManager, RouteDataBetriebsarten, RouteApp> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.betriebsarten",
			String.raw`schule/betriebsarten/:id(\d+)?`, BetriebsartenApp, BetriebsartenAuswahl, new RouteDataBetriebsarten());
		super.mode = ServerMode.STABLE;
		super.text = "Betriebsarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeBetriebsartenDaten,
			routeBetriebsartenNeu,
			routeBetriebsartenGruppenprozesse,
		];
		super.defaultChild = routeBetriebsartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeBetriebsarten = new RouteBetriebsarten();
