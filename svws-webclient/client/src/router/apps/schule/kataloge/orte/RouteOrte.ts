import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { OrteListeManager } from "../../../../../../../ui/src/ui/manager/kataloge/OrteListeManager";
import { routeOrteDaten } from "~/router/apps/schule/kataloge/orte/RouteOrteDaten";
import { routeOrteGruppenprozesse } from "~/router/apps/schule/kataloge/orte/RouteOrteGruppenprozesse";
import { routeOrteNeu } from "~/router/apps/schule/kataloge/orte/RouteOrteNeu";
import { RouteDataOrte } from "~/router/apps/schule/kataloge/orte/RouteDataOrte";

const OrteAuswahl = () => import("~/components/schule/kataloge/orte/OrteAuswahl.vue");
const OrteApp = () => import("~/components/schule/kataloge/orte/OrteApp.vue");

export class RouteOrte extends RouteAuswahlNode<OrteListeManager, RouteDataOrte, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.orte", "schule/orte/:id(\\d+)?", OrteApp, OrteAuswahl, new RouteDataOrte());
		super.mode = ServerMode.STABLE;
		super.text = "Orte";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeOrteDaten,
			routeOrteNeu,
			routeOrteGruppenprozesse,
		];
		super.defaultChild = routeOrteDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeOrte = new RouteOrte();
