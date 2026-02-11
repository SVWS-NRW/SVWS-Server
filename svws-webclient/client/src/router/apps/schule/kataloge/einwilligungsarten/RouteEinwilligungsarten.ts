import type { RouteParams } from "vue-router";
import type { EinwilligungsartenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeEinwilligungsartenDaten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenDaten";
import { RouteDataEinwilligungsarten } from "./RouteDataEinwilligungsarten";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeEinwilligungsartenNeu } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenNeu";
import { routeEinwilligungsartenGruppenprozesse } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenGruppenprozesse";

const EinwilligungsartenAuswahl = () => import("~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenAuswahl.vue");
const EinwilligungsartenApp = () => import("~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenApp.vue");

export class RouteEinwilligungsarten extends RouteAuswahlNode<EinwilligungsartenListeManager, RouteDataEinwilligungsarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.einwilligungsarten",
			"schule/einwilligungsarten/:id(\\d+)?", EinwilligungsartenApp, EinwilligungsartenAuswahl, new RouteDataEinwilligungsarten());
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
