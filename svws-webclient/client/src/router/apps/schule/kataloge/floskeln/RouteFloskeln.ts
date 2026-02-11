import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import type { FloskelnListeManager } from "@ui";
import { routeFloskelnDaten } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnDaten";
import { routeFloskelnNeu } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnNeu";
import { routeFloskelnGruppenprozesse } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnGruppenprozesse";
import { RouteDataFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteDataFloskeln";

const FloskelnApp = () => import("~/components/schule/kataloge/floskeln/FloskelnApp.vue");
const FloskelnAuswahl = () => import("~/components/schule/kataloge/floskeln/FloskelnAuswahl.vue");

export class RouteFloskeln extends RouteAuswahlNode<FloskelnListeManager, RouteDataFloskeln, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskeln",
			`schule/floskeln/:id(\\d+)?`, FloskelnApp, FloskelnAuswahl, new RouteDataFloskeln());
		super.mode = ServerMode.DEV;
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
