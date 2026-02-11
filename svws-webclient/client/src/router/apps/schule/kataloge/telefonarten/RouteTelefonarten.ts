import type { RouteParams } from "vue-router";
import type { TelefonartenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeTelefonartenDaten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenDaten";
import { routeTelefonartenGruppenprozesse } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenGruppenprozesse";
import { routeTelefonartenNeu } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenNeu";
import { RouteDataTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteDataTelefonarten";

const TelefonartenAuswahl = () => import("~/components/schule/kataloge/telefonarten/TelefonartenAuswahl.vue");
const TelefonartenApp = () => import("~/components/schule/kataloge/telefonarten/TelefonartenApp.vue");

export class RouteTelefonarten extends RouteAuswahlNode<TelefonartenListeManager, RouteDataTelefonarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.telefonarten", "schule/telefonarten/:id(\\d+)?", TelefonartenApp, TelefonartenAuswahl, new RouteDataTelefonarten());
		super.mode = ServerMode.DEV;
		super.text = "Telefonarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeTelefonartenDaten,
			routeTelefonartenNeu,
			routeTelefonartenGruppenprozesse,
		];
		super.defaultChild = routeTelefonartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeTelefonarten = new RouteTelefonarten();
