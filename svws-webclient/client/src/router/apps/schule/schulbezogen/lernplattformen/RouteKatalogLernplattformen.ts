import type { RouteParams } from "vue-router";
import type { LernplattformListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataKatalogLernplattformen } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteDataKatalogLernplattformen";
import { routeKatalogLernplattformenDaten } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformenDaten";
import { routeKatalogLernplattformenNeu } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformenNeu";
import { routeKatalogLernplattformenGruppenprozesse } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformenGruppenprozesse";

const SLernplattformenAuswahl = () => import("~/components/schule/schulbezogen/lernplattformen/SLernplattformenAuswahl.vue");
const SLernplattformenApp = () => import("~/components/schule/schulbezogen/lernplattformen/SLernplattformenApp.vue");

export class RouteKatalogLernplattformen extends RouteAuswahlNode<LernplattformListeManager, RouteDataKatalogLernplattformen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.lernplattformen", "schule/lernplattformen/:id(\\d+)?", SLernplattformenApp, SLernplattformenAuswahl, new RouteDataKatalogLernplattformen());
		super.mode = ServerMode.DEV;
		super.text = "Lernplattformen";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeKatalogLernplattformenDaten,
			routeKatalogLernplattformenNeu,
			routeKatalogLernplattformenGruppenprozesse,
		];
		super.defaultChild = routeKatalogLernplattformenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeKatalogLernplattformen = new RouteKatalogLernplattformen();
