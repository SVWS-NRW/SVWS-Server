import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteKatalogVermerkarten } from "./RouteKatalogVermerkarten";
import { routeKatalogVermerkarten } from "./RouteKatalogVermerkarten";
import type { SchuleVermerkartNeuProps } from "~/components/schule/kataloge/vermerke/SVermerkartNeuProps";

const SVermerkartNeu = () => import("~/components/schule/kataloge/vermerke/SVermerkartNeu.vue");

export class RouteKatalogVermerkartNeu extends RouteNode<any, RouteKatalogVermerkarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.vermerkarten.neu", "neu", SVermerkartNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkart Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SchuleVermerkartNeuProps {
		return {
			manager: () => routeKatalogVermerkarten.data.manager,
			add: routeKatalogVermerkarten.data.add,
			gotoDefaultView: routeKatalogVermerkarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKatalogVermerkartNeu = new RouteKatalogVermerkartNeu();
