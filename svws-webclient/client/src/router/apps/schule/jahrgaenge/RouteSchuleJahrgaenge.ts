import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, type JahrgangListeManager, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeSchuleJahrgaengeDaten } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaengeDaten";
import { RouteDataSchuleJahrgaenge } from "./RouteDataSchuleJahrgaenge";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeSchuleJahrgangNeu } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangNeu";
import { routeSchuleJahrgangGruppenprozesse } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangGruppenprozesse";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";

const SJahrgaengeAuswahl = () => import("~/components/schule/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/schule/jahrgaenge/SJahrgaengeApp.vue")

export class RouteSchuleJahrgaenge extends RouteAuswahlNode<JahrgangListeManager, RouteDataSchuleJahrgaenge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN ], "schule.jahrgaenge", "schule/jahrgaenge/:id(\\d+)?", SJahrgaengeApp, SJahrgaengeAuswahl, new RouteDataSchuleJahrgaenge());
		super.mode = ServerMode.DEV;
		super.text = "Jahrgänge";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeSchuleJahrgaengeDaten,
			routeSchuleJahrgangNeu,
			routeSchuleJahrgangGruppenprozesse,
		];
		super.defaultChild = routeSchuleJahrgaengeDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeSchuleJahrgaenge = new RouteSchuleJahrgaenge();
