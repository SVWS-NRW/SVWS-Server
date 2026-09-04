import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteDataFachklassen";
import { routeFachklassenDaten } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenDaten";
import { routeFachklassenGruppenprozesse } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenGruppenprozesse";
import { routeFachklassenNeu } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenNeu";
import FachklassenApp from "~/components/schule/kataloge/fachklassen/FachklassenApp.vue";
import FachklassenAuswahl from "~/components/schule/kataloge/fachklassen/FachklassenAuswahl.vue";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { FachklassenListeManager } from "@ui/ui/manager/kataloge/FachklassenListeManager";

export class RouteFachklassen extends RouteAuswahlNode<FachklassenListeManager, RouteDataFachklassen, RouteApp> {
	public constructor() {
		super([Schulform.BK, Schulform.SB],
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.fachklassen",
			String.raw`schule/fachklassen/:id(\d+)?`,
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
