import type { RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { TeilleistungsartenListeManager } from "~/components/schule/kataloge/teilleistungsarten/manager/TeilleistungsartenListeManager";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteDataTeilleistungsarten } from "./RouteDataTeilleistungsarten";
import { routeTeilleistungsartenNeu } from "./RouteTeilleistungsartenNeu";
import { routeTeilleistungsartenDaten } from "./RouteTeilleistungsartenDaten";
import { routeTeilleistungsartenGruppenprozesse } from "./RouteTeilleistungsartenGruppenprozesse";

import TeilleistungsartenApp from "~/components/schule/kataloge/teilleistungsarten/TeilleistungsartenApp.vue";
import TeilleistungsartenAuswahl from "~/components/schule/kataloge/teilleistungsarten/TeilleistungsartenAuswahl.vue";

export class RouteTeilleistungsarten extends RouteAuswahlNode<TeilleistungsartenListeManager, RouteDataTeilleistungsarten, RouteApp> {
	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.teilleistungsarten",
			"schule/teilleistungsarten/:id(\\d+)?",
			TeilleistungsartenApp,
			TeilleistungsartenAuswahl,
			new RouteDataTeilleistungsarten());

		super.mode = ServerMode.STABLE;
		super.text = "Teilleistungsarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeTeilleistungsartenDaten,
			routeTeilleistungsartenNeu,
			routeTeilleistungsartenGruppenprozesse,
		];
		super.defaultChild = routeTeilleistungsartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeTeilleistungsarten = new RouteTeilleistungsarten();
