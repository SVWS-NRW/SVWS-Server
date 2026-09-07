import type { RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";

import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeEinwilligungsartenDaten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenDaten";
import { routeEinwilligungsartenGruppenprozesse } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenGruppenprozesse";
import { routeEinwilligungsartenNeu } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenNeu";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";

import { RouteDataEinwilligungsarten } from "./RouteDataEinwilligungsarten";

const EinwilligungsartenAuswahl = () => import("~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenAuswahl.vue");
const EinwilligungsartenApp = () => import("~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenApp.vue");

export class RouteEinwilligungsarten extends RouteAuswahlNode<EinwilligungsartenListeManager, RouteDataEinwilligungsarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.einwilligungsarten",
			String.raw`schule/einwilligungsarten/:id(\d+)?`, EinwilligungsartenApp, EinwilligungsartenAuswahl, new RouteDataEinwilligungsarten());
		super.mode = ServerMode.STABLE;
		super.text = "Einwilligungsarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeEinwilligungsartenDaten,
			routeEinwilligungsartenNeu,
			routeEinwilligungsartenGruppenprozesse,
		];
		super.defaultChild = routeEinwilligungsartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (this.data.manager.hasDaten() === false) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeEinwilligungsarten = new RouteEinwilligungsarten();
