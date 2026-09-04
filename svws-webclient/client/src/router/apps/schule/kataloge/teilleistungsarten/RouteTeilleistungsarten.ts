import type { RouteParams } from "vue-router";
import type { TeilleistungsartenListeManager } from "~/states/teilleistungsarten/TeilleistungsartenListeManager";
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
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

export class RouteTeilleistungsarten extends RouteAuswahlNode<TeilleistungsartenListeManager, RouteDataTeilleistungsarten, RouteApp> {
	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.teilleistungsarten",
			String.raw`schule/teilleistungsarten/:id(\d+)?`,
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
