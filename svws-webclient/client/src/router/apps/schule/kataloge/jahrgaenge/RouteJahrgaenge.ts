import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeJahrgaengeDaten } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaengeDaten";
import { RouteDataJahrgaenge } from "./RouteDataJahrgaenge";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeJahrgaengeNeu } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaengeNeu";
import { routeJahrgaengeGruppenprozesse } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaengeGruppenprozesse";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";

const JahrgaengeAuswahl = () => import("~/components/schule/kataloge/jahrgaenge/JahrgaengeAuswahl.vue");
const JahrgaengeApp = () => import("~/components/schule/kataloge/jahrgaenge/JahrgaengeApp.vue");

export class RouteJahrgaenge extends RouteAuswahlNode<JahrgaengeListeManager, RouteDataJahrgaenge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.jahrgaenge", String.raw`schule/jahrgaenge/:id(\d+)?`, JahrgaengeApp, JahrgaengeAuswahl, new RouteDataJahrgaenge());
		super.mode = ServerMode.STABLE;
		super.text = "Jahrgänge";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeJahrgaengeDaten,
			routeJahrgaengeNeu,
			routeJahrgaengeGruppenprozesse,
		];
		super.defaultChild = routeJahrgaengeDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeJahrgaenge = new RouteJahrgaenge();
