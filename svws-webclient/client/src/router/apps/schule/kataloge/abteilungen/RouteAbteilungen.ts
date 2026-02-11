import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { AbteilungenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataAbteilungen } from "./RouteDataAbteilungen";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { routeAbteilungenDaten } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenDaten";
import { routeAbteilungenNeu } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenNeu";
import { routeAbteilungenGruppenprozesse } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenGruppenprozesse";

const AbteilungenApp = () => import("~/components/schule/kataloge/abteilungen/AbteilungenApp.vue");
const AbteilungenAuswahl = () => import("~/components/schule/kataloge/abteilungen/AbteilungenAuswahl.vue");

export class RouteAbteilungen extends RouteAuswahlNode<AbteilungenListeManager, RouteDataAbteilungen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.abteilungen",
			"schule/abteilungen/:id(\\d+)?", AbteilungenApp, AbteilungenAuswahl, new RouteDataAbteilungen());
		super.mode = ServerMode.DEV;
		super.text = "Abteilungen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeAbteilungenDaten,
			routeAbteilungenNeu,
			routeAbteilungenGruppenprozesse,
		];
		super.defaultChild = routeAbteilungenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (this.data.manager.hasDaten() === false) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeAbteilungen = new RouteAbteilungen();
