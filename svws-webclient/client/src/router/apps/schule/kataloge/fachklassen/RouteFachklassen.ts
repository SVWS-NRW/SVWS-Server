import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { FachklassenListeManager } from "@ui";
import type { RouteApp } from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteDataFachklassen";
import { routeFachklassenDaten } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenDaten";
import { routeFachklassenGruppenprozesse } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenGruppenprozesse";
import { routeFachklassenNeu } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenNeu";

import FachklassenApp from "~/components/schule/kataloge/fachklassen/FachklassenApp.vue";
import FachklassenAuswahl from "~/components/schule/kataloge/fachklassen/FachklassenAuswahl.vue";

export class RouteFachklassen extends RouteAuswahlNode<FachklassenListeManager, RouteDataFachklassen, RouteApp> {
	public constructor() {
		super([Schulform.BK, Schulform.SB],
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.fachklassen",
			"schule/fachklassen/:id(\\d+)?",
			FachklassenApp,
			FachklassenAuswahl,
			new RouteDataFachklassen());
		super.mode = ServerMode.DEV;
		super.text = "Fachklassen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeFachklassenDaten,
			routeFachklassenNeu,
			routeFachklassenGruppenprozesse,
		];
		super.defaultChild = routeFachklassenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async () => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeFachklassen = new RouteFachklassen();
