import type { SchwerpunkteListeManager } from "@ui";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteParams } from "vue-router";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeSchwerpunkteNeu } from "./RouteSchwerpunkteNeu";
import { routeSchwerpunkteDaten } from "./RouteSchwerpunkteDaten";
import { RouteDataSchwerpunkte } from "./RouteDataSchwerpunkte";
import { routeSchwerpunkteGruppenprozesse } from "./RouteSchwerpunkteGruppenprozesse";

import SchwerpunkteApp from "~/components/schule/kataloge/schwerpunkte/SchwerpunkteApp.vue";
import SchwerpunkteAuswahl from "~/components/schule/kataloge/schwerpunkte/SchwerpunkteAuswahl.vue";

export class RouteSchwerpunkte extends RouteAuswahlNode<SchwerpunkteListeManager, RouteDataSchwerpunkte, RouteApp> {
	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schwerpunkte", "schule/schwerpunkte/:id(\\d+)?", SchwerpunkteApp, SchwerpunkteAuswahl, new RouteDataSchwerpunkte());
		super.mode = ServerMode.DEV;
		super.text = "Schwerpunkte";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeSchwerpunkteDaten,
			routeSchwerpunkteNeu,
			routeSchwerpunkteGruppenprozesse,
		];
		super.defaultChild = routeSchwerpunkteDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		/*		if (!this.data.manager.hasDaten()) {
			return;
		}*/
		return this.getRouteSelectedChild();
	};

}

export const routeSchwerpunkte = new RouteSchwerpunkte();
