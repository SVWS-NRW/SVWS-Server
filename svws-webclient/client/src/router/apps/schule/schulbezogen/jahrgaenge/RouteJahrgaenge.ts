import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { JahrgaengeListeManager } from "@ui";

import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeJahrgaengeDaten } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaengeDaten";
import { RouteDataJahrgaenge } from "./RouteDataJahrgaenge";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeJahrgaengeNeu } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaengeNeu";
import { routeJahrgaengeGruppenprozesse } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaengeGruppenprozesse";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";

const SJahrgaengeAuswahl = () => import("~/components/schule/schulbezogen/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/schule/schulbezogen/jahrgaenge/SJahrgaengeApp.vue")

export class RouteJahrgaenge extends RouteAuswahlNode<JahrgaengeListeManager, RouteDataJahrgaenge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.jahrgaenge", "schule/jahrgaenge/:id(\\d+)?", SJahrgaengeApp, SJahrgaengeAuswahl, new RouteDataJahrgaenge());
		super.mode = ServerMode.DEV;
		super.text = "Jahrgänge";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeJahrgaengeDaten,
			routeJahrgaengeNeu,
			routeJahrgaengeGruppenprozesse,
		];
		super.defaultChild = routeJahrgaengeDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeJahrgaenge = new RouteJahrgaenge();
