import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { SErzieherartenNeuProps } from "~/components/schule/allgemein/erzieherarten/SErzieherartenNeuProps";
import type { RouteKatalogErzieherarten } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherarten";
import { routeKatalogErzieherarten } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherarten";
import { api } from "~/router/Api";

const SErzieherartenNeu = () => import("~/components/schule/allgemein/erzieherarten/SErzieherartenNeu.vue");

export class RouteKatalogErzieherartenNeu extends RouteNode<any, RouteKatalogErzieherarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.erzieherarten.neu", "neu", SErzieherartenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erzieherarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SErzieherartenNeuProps {
		return {
			manager: () => routeKatalogErzieherarten.data.manager,
			add: routeKatalogErzieherarten.data.add,
			gotoDefaultView: routeKatalogErzieherarten.data.gotoDefaultView,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKatalogErzieherartenNeu = new RouteKatalogErzieherartenNeu();
