import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { BetriebeListeManager } from "../../../../../../../ui/src/ui/manager/kataloge/BetriebeListeManager";
import { routeBetriebeDaten } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeDaten";
import { routeBetriebeGruppenprozesse } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeGruppenprozesse";
import { routeBetriebeNeu } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeNeu";
import { RouteDataBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteDataBetriebe";

const BetriebeAuswahl = () => import("~/components/schule/kataloge/betriebe/BetriebeAuswahl.vue");
const BetriebeApp = () => import("~/components/schule/kataloge/betriebe/BetriebeApp.vue");

export class RouteBetriebe extends RouteAuswahlNode<BetriebeListeManager, RouteDataBetriebe, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.betriebe", "schule/betriebe/:id(\\d+)?", BetriebeApp, BetriebeAuswahl, new RouteDataBetriebe());
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
