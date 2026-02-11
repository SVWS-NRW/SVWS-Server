import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { OrtsteileListeManager } from "../../../../../../../ui/src/ui/manager/kataloge/OrtsteileListeManager";
import { routeOrtsteileDaten } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileDaten";
import { routeOrtsteileGruppenprozesse } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileGruppenprozesse";
import { routeOrtsteileNeu } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileNeu";
import { RouteDataOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteDataOrtsteile";

const OrtsteileAuswahl = () => import("~/components/schule/kataloge/ortsteile/OrtsteileAuswahl.vue");
const OrtsteileApp = () => import("~/components/schule/kataloge/ortsteile/OrtsteileApp.vue");

export class RouteOrtsteile extends RouteAuswahlNode<OrtsteileListeManager, RouteDataOrtsteile, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.ortsteile", "schule/ortsteile/:id(\\d+)?", OrtsteileApp, OrtsteileAuswahl, new RouteDataOrtsteile());
		super.mode = ServerMode.STABLE;
		super.text = "Ortsteile";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeOrtsteileDaten,
			routeOrtsteileNeu,
			routeOrtsteileGruppenprozesse,
		];
		super.defaultChild = routeOrtsteileDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeOrtsteile = new RouteOrtsteile();
