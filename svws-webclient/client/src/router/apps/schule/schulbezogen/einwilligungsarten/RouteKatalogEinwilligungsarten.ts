import type { RouteParams } from "vue-router";
import type { EinwilligungsartenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeKatalogEinwilligungsartenDaten } from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteKatalogEinwilligungsartenDaten";
import { RouteDataKatalogEinwilligungsarten } from "./RouteDataKatalogEinwilligungsarten";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKatalogEinwilligungsartenNeu } from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteKatalogEinwilligungsartenNeu";
import { routeKatalogEinwilligungsartenGruppenprozesse } from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteKatalogEinwilligungsartenGruppenprozesse";

const SEinwilligungsartenAuswahl = () => import("~/components/schule/schulbezogen/einwilligungsarten/SEinwilligungsartenAuswahl.vue");
const SEinwilligungsartenApp = () => import("~/components/schule/schulbezogen/einwilligungsarten/SEinwilligungsartenApp.vue");

export class RouteKatalogEinwilligungsarten extends RouteAuswahlNode<EinwilligungsartenListeManager, RouteDataKatalogEinwilligungsarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.einwilligungsarten", "schule/einwilligungsarten/:id(\\d+)?", SEinwilligungsartenApp, SEinwilligungsartenAuswahl, new RouteDataKatalogEinwilligungsarten());
		super.mode = ServerMode.DEV;
		super.text = "Einwilligungsarten";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeKatalogEinwilligungsartenDaten,
			routeKatalogEinwilligungsartenNeu,
			routeKatalogEinwilligungsartenGruppenprozesse,
		];
		super.defaultChild = routeKatalogEinwilligungsartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (this.data.manager.hasDaten() === false)
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeKatalogEinwilligungsarten = new RouteKatalogEinwilligungsarten();
