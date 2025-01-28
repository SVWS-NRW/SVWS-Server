import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteKatalogEinwilligungsarten} from "~/router/apps/schule/einwilligungsarten/RouteKatalogEinwilligungsarten";
import {routeKatalogEinwilligungsarten} from "~/router/apps/schule/einwilligungsarten/RouteKatalogEinwilligungsarten";
import type {SchuleEinwilligungsartenNeuProps} from "~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenNeuProps";

const SEinwilligungsartNeu = () => import("~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenNeu.vue");

export class RouteKatalogEinwilligungsartenNeu extends RouteNode<any, RouteKatalogEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.einwilligungsarten.neu", "neu", SEinwilligungsartNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsart Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SchuleEinwilligungsartenNeuProps {
		return {
			manager: () => routeKatalogEinwilligungsarten.data.manager,
			add: routeKatalogEinwilligungsarten.data.add,
			gotoDefaultView: routeKatalogEinwilligungsarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKatalogEinwilligungsartenNeu = new RouteKatalogEinwilligungsartenNeu();
