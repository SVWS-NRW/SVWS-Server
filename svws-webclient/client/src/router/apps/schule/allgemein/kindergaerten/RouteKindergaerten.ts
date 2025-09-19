import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { KindergaertenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataKindergaerten } from "~/router/apps/schule/allgemein/kindergaerten/RouteDataKindergaerten";
import { routeKindergaertenDaten } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaertenDaten";
import { routeKindergaertenNeu } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaertenNeu";
import { routeKindergaertenGruppenprozesse } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaertenGruppenprozesse";


const SKindergartenApp = () => import("~/components/schule/allgemein/kindergaerten/SKindergaertenApp.vue");
const SKindergaertenAuswahl = () => import("~/components/schule/allgemein/kindergaerten/SKindergaertenAuswahl.vue");

export class RouteKindergaerten extends RouteAuswahlNode<KindergaertenListeManager, RouteDataKindergaerten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.kindergaerten",
			"schule/kindergaerten/:id(\\d+)?", SKindergartenApp, SKindergaertenAuswahl, new RouteDataKindergaerten());
		super.mode = ServerMode.DEV;
		super.text = "Kindergärten";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeKindergaertenDaten,
			routeKindergaertenNeu,
			routeKindergaertenGruppenprozesse,
		];
		super.defaultChild = routeKindergaertenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeKindergaerten = new RouteKindergaerten();
