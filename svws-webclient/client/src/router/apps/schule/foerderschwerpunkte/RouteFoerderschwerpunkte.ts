import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { FoerderschwerpunkteListeManager } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { routeFoerderschwerpunkteNeu } from "~/router/apps/schule/foerderschwerpunkte/RouteFoerderschwerpunkteNeu";
import { routeFoerderschwerpunkteDaten } from "~/router/apps/schule/foerderschwerpunkte/RouteFoerderschwerpunkteDaten";
import { routeFoerderschwerpunkteGruppenprozesse } from "~/router/apps/schule/foerderschwerpunkte/RouteFoerderschwerpunkteGruppenprozesse";
import { RouteDataFoerderschwerpunkte } from "~/router/apps/schule/foerderschwerpunkte/RouteDataFoerderschwerpunkte";

const SFoerderschwerpunkteApp = () => import("~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteApp.vue");
const SFoerderschwerpunkteAuswahl = () => import("~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahl.vue");

export class RouteFoerderschwerpunkte extends RouteAuswahlNode<FoerderschwerpunkteListeManager, RouteDataFoerderschwerpunkte, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.foerderschwerpunkte",
			"schule/foerderschwerpunkte/:id(\\d+)?", SFoerderschwerpunkteApp, SFoerderschwerpunkteAuswahl, new RouteDataFoerderschwerpunkte());
		super.mode = ServerMode.DEV;
		super.text = "Förderschwerpunkte";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeFoerderschwerpunkteDaten,
			routeFoerderschwerpunkteNeu,
			routeFoerderschwerpunkteGruppenprozesse,
		];
		super.defaultChild = routeFoerderschwerpunkteDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeFoerderschwerpunkte = new RouteFoerderschwerpunkte();
