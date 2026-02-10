import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { EntlassgruendeListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteDataEntlassgruende";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { routeEntlassgruendeDaten } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeDaten";
import { routeEntlassgruendeGruppenprozesse } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeGruppenprozesse";
import { routeEntlassgruendeNeu } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruendeNeu";

const EntlassgruendeApp = () => import("~/components/schule/kataloge/entlassgruende/EntlassgruendeApp.vue");
const EntlassgruendeAuswahl = () => import("~/components/schule/kataloge/entlassgruende/EntlassgruendeAuswahl.vue");

export class RouteEntlassgruende extends RouteAuswahlNode<EntlassgruendeListeManager, RouteDataEntlassgruende, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.entlassgruende",
			"schule/entlassgruende/:id(\\d+)?", EntlassgruendeApp, EntlassgruendeAuswahl, new RouteDataEntlassgruende());
		super.mode = ServerMode.STABLE;
		super.text = "Entlassgründe";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeEntlassgruendeDaten,
			routeEntlassgruendeNeu,
			routeEntlassgruendeGruppenprozesse,
		];
		super.defaultChild = routeEntlassgruendeDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeEntlassgruende = new RouteEntlassgruende();
