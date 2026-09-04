import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeBetriebeDaten } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeDaten";
import { routeBetriebeGruppenprozesse } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeGruppenprozesse";
import { routeBetriebeNeu } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebeNeu";
import { RouteDataBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteDataBetriebe";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { BetriebeListeManager } from "@ui/ui/manager/kataloge/BetriebeListeManager";

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
