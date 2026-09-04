import type { RouteLocationNormalized } from "vue-router";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import type { RouteFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import { routeFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import type { FloskelnNeuProps } from "~/components/schule/kataloge/floskeln/FloskelnNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const FloskelnNeu = () => import("~/components/schule/kataloge/floskeln/FloskelnNeu.vue");

export class RouteFloskelnNeu extends RouteNode<any, RouteFloskeln> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskeln.neu", "neu", FloskelnNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Floskeln";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FloskelnNeuProps {
		return {
			manager: () => routeFloskeln.data.manager,
			add: routeFloskeln.data.add,
			goToDefaultView: routeFloskeln.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFloskelnNeu = new RouteFloskelnNeu();
