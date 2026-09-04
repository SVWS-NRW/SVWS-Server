import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeOrteDaten } from "~/router/apps/schule/kataloge/orte/RouteOrteDaten";
import { routeOrteGruppenprozesse } from "~/router/apps/schule/kataloge/orte/RouteOrteGruppenprozesse";
import { routeOrteNeu } from "~/router/apps/schule/kataloge/orte/RouteOrteNeu";
import { RouteDataOrte } from "~/router/apps/schule/kataloge/orte/RouteDataOrte";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { OrteListeManager } from "@ui/ui/manager/kataloge/OrteListeManager";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";

const OrteAuswahl = () => import("~/components/schule/kataloge/orte/OrteAuswahl.vue");
const OrteApp = () => import("~/components/schule/kataloge/orte/OrteApp.vue");

export class RouteOrte extends RouteAuswahlNode<OrteListeManager, RouteDataOrte, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.orte", String.raw`schule/orte/:id(\d+)?`, OrteApp, OrteAuswahl, new RouteDataOrte());
		super.mode = ServerMode.STABLE;
		super.text = "Orte";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeOrteDaten,
			routeOrteNeu,
			routeOrteGruppenprozesse,
		];
		super.defaultChild = routeOrteDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeOrte = new RouteOrte();
