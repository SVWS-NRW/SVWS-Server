import type { RouteParams } from "vue-router";
import type { ErzieherartListeManager } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataKatalogErzieherarten } from "~/router/apps/schule/erzieherarten/RouteDataKatalogErzieherarten";
import { routeKatalogErzieherartenDaten } from "~/router/apps/schule/erzieherarten/RouteKatalogErzieherartenDaten";
import { routeKatalogErzieherartenNeu } from "~/router/apps/schule/erzieherarten/RouteKatalogErzieherartenNeu";
import { routeKatalogErzieherartenGruppenprozesse } from "~/router/apps/schule/erzieherarten/RouteKatalogErzieherartenGruppenprozesse";

const SErzieherartenAuswahl = () => import("~/components/schule/kataloge/erzieherarten/SErzieherartenAuswahl.vue");
const SErzieherartenApp = () => import("~/components/schule/kataloge/erzieherarten/SErzieherartenApp.vue");

export class RouteKatalogErzieherarten extends RouteAuswahlNode<ErzieherartListeManager, RouteDataKatalogErzieherarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.erzieherarten", "schule/erzieherarten/:id(\\d+)?", SErzieherartenApp, SErzieherartenAuswahl, new RouteDataKatalogErzieherarten());
		super.mode = ServerMode.DEV;
		super.text = "Erzieherarten";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeKatalogErzieherartenDaten,
			routeKatalogErzieherartenNeu,
			routeKatalogErzieherartenGruppenprozesse,
		];
		super.defaultChild = routeKatalogErzieherartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeKatalogErzieherarten = new RouteKatalogErzieherarten();
