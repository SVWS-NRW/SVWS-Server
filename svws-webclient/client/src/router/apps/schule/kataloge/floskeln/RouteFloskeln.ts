import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { routeFloskelnDaten } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnDaten";
import { routeFloskelnNeu } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnNeu";
import { routeFloskelnGruppenprozesse } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnGruppenprozesse";
import { RouteDataFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteDataFloskeln";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { FloskelnListeManager } from "@ui/ui/manager/kataloge/FloskelnListeManager";

const FloskelnApp = () => import("~/components/schule/kataloge/floskeln/FloskelnApp.vue");
const FloskelnAuswahl = () => import("~/components/schule/kataloge/floskeln/FloskelnAuswahl.vue");

export class RouteFloskeln extends RouteAuswahlNode<FloskelnListeManager, RouteDataFloskeln, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskeln",
			String.raw`schule/floskeln/:id(\d+)?`, FloskelnApp, FloskelnAuswahl, new RouteDataFloskeln());
		super.mode = ServerMode.STABLE;
		super.text = "Floskeln";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeFloskelnDaten,
			routeFloskelnNeu,
			routeFloskelnGruppenprozesse,
		];
		super.defaultChild = routeFloskelnDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeFloskeln = new RouteFloskeln();
