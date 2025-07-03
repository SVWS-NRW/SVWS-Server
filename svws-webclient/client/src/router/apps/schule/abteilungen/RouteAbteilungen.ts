import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { AbteilungenListeManager } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataAbteilungen } from "./RouteDataAbteilungen";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { routeAbteilungenDaten } from "~/router/apps/schule/abteilungen/RouteAbteilungenDaten";
import { routeAbteilungenNeu } from "~/router/apps/schule/abteilungen/RouteAbteilungenNeu";
import { routeAbteilungenGruppenprozesse } from "~/router/apps/schule/abteilungen/RouteAbteilungenGruppenprozesse";

const SAbteilungenApp = () => import("~/components/schule/kataloge/abteilungen/SAbteilungenApp.vue");
const SAbteilungenAuswahl = () => import("~/components/schule/kataloge/abteilungen/SAbteilungenAuswahl.vue")

export class RouteAbteilungen extends RouteAuswahlNode<AbteilungenListeManager, RouteDataAbteilungen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.abteilungen",
			"schule/abteilungen/:id(\\d+)?", SAbteilungenApp, SAbteilungenAuswahl, new RouteDataAbteilungen());
		super.mode = ServerMode.DEV;
		super.text = "Abteilungen";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeAbteilungenDaten,
			routeAbteilungenNeu,
			routeAbteilungenGruppenprozesse,
		];
		super.defaultChild = routeAbteilungenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (this.data.manager.hasDaten() === false)
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeAbteilungen = new RouteAbteilungen();
