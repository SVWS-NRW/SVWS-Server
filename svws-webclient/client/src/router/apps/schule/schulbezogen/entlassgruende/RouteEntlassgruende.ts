import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { EntlassgruendeListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataEntlassgruende } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteDataEntlassgruende";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { routeEntlassgruendeDaten } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteEntlassgruendeDaten";
import { routeEntlassgruendeGruppenprozesse } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteEntlassgruendeGruppenprozesse";
import { routeEntlassgruendeNeu } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteEntlassgruendeNeu";

const SEntlassgruendeApp = () => import("~/components/schule/schulbezogen/entlassgruende/SEntlassgruendeApp.vue");
const SEntlassgruendeAuswahl = () => import("~/components/schule/schulbezogen/entlassgruende/SEntlassgruendeAuswahl.vue");

export class RouteEntlassgruende extends RouteAuswahlNode<EntlassgruendeListeManager, RouteDataEntlassgruende, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.entlassgruende",
			"schule/entlassgruende/:id(\\d+)?", SEntlassgruendeApp, SEntlassgruendeAuswahl, new RouteDataEntlassgruende());
		super.mode = ServerMode.DEV;
		super.text = "Entlassgründe";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeEntlassgruendeDaten,
			routeEntlassgruendeNeu,
			routeEntlassgruendeGruppenprozesse
		];
		super.defaultChild = routeEntlassgruendeDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};

}

export const routeEntlassgruende = new RouteEntlassgruende();
