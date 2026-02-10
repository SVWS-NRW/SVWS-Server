import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { KindergaertenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteDataKindergaerten";
import { routeKindergaertenDaten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaertenDaten";
import { routeKindergaertenNeu } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaertenNeu";
import { routeKindergaertenGruppenprozesse } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaertenGruppenprozesse";


const KindergartenApp = () => import("~/components/schule/kataloge/kindergaerten/KindergaertenApp.vue");
const KindergaertenAuswahl = () => import("~/components/schule/kataloge/kindergaerten/KindergaertenAuswahl.vue");

export class RouteKindergaerten extends RouteAuswahlNode<KindergaertenListeManager, RouteDataKindergaerten, RouteApp> {

	public constructor() {
		super([Schulform.G, Schulform.PS, Schulform.S, Schulform.V, Schulform.FW, Schulform.WF],
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.kindergaerten",
			"schule/kindergaerten/:id(\\d+)?", KindergartenApp, KindergaertenAuswahl, new RouteDataKindergaerten());
		super.mode = ServerMode.STABLE;
		super.text = "Kindergärten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeKindergaertenDaten,
			routeKindergaertenNeu,
			routeKindergaertenGruppenprozesse,
		];
		super.defaultChild = routeKindergaertenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeKindergaerten = new RouteKindergaerten();
