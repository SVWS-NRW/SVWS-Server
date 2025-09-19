import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { HaltestellenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataHaltestellen } from "~/router/apps/schule/allgemein/haltestellen/RouteDataHaltestellen";
import { routeHaltestellenDaten } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellenDaten";
import { routeHaltestellenNeu } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellenNeu";
import { routeHaltestellenGruppenprozesse } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellenGruppenprozesse";

const SHaltestellenApp = () => import("~/components/schule/allgemein/haltestellen/SHaltestellenApp.vue");
const SHaltestellenAuswahl = () => import("~/components/schule/allgemein/haltestellen/SHaltestellenAuswahl.vue");

export class RouteHaltestellen extends RouteAuswahlNode<HaltestellenListeManager, RouteDataHaltestellen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.haltestellen",
			"schule/haltestellen/:id(\\d+)?", SHaltestellenApp, SHaltestellenAuswahl, new RouteDataHaltestellen());
		super.mode = ServerMode.DEV;
		super.text = "Haltestellen";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeHaltestellenDaten,
			routeHaltestellenNeu,
			routeHaltestellenGruppenprozesse,
		];
		super.defaultChild = routeHaltestellenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeHaltestellen = new RouteHaltestellen();
