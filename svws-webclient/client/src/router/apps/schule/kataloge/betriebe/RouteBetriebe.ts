import type { RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { BetriebeListeManager } from "@ui/ui/manager/kataloge/BetriebeListeManager";

import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeBetriebeDaten } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeDaten";
import { routeBetriebeGruppenprozesse } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeGruppenprozesse";
import { routeBetriebeNeu } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeNeu";
import { RouteDataBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteDataBetriebe";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";

const BetriebeAuswahl = () => import("~/components/schule/kataloge/betriebe/BetriebeAuswahl.vue");
const BetriebeApp = () => import("~/components/schule/kataloge/betriebe/BetriebeApp.vue");

export class RouteBetriebe extends RouteAuswahlNode<BetriebeListeManager, RouteDataBetriebe, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.betriebe", String.raw`schule/betriebe/:id(\d+)?`, BetriebeApp, BetriebeAuswahl, new RouteDataBetriebe());
		super.mode = ServerMode.STABLE;
		super.text = "Betriebe";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeBetriebeDaten,
			routeBetriebeNeu,
			routeBetriebeGruppenprozesse,
		];
		super.defaultChild = routeBetriebeDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeBetriebe = new RouteBetriebe();
