import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteDataHaltestellen";
import { routeHaltestellenDaten } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellenDaten";
import { routeHaltestellenNeu } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellenNeu";
import { routeHaltestellenGruppenprozesse } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellenGruppenprozesse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { HaltestellenListeManager } from "@ui/ui/manager/kataloge/HaltestellenListeManager";

const HaltestellenApp = () => import("~/components/schule/kataloge/haltestellen/HaltestellenApp.vue");
const HaltestellenAuswahl = () => import("~/components/schule/kataloge/haltestellen/HaltestellenAuswahl.vue");

export class RouteHaltestellen extends RouteAuswahlNode<HaltestellenListeManager, RouteDataHaltestellen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.haltestellen",
			String.raw`schule/haltestellen/:id(\d+)?`, HaltestellenApp, HaltestellenAuswahl, new RouteDataHaltestellen());
		super.mode = ServerMode.STABLE;
		super.text = "Haltestellen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeHaltestellenDaten,
			routeHaltestellenNeu,
			routeHaltestellenGruppenprozesse,
		];
		super.defaultChild = routeHaltestellenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeHaltestellen = new RouteHaltestellen();
