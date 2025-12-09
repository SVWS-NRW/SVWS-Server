import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import type { RouteFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import { routeFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import type { FloskelnNeuProps } from "~/components/schule/kataloge/floskeln/FloskelnNeuProps";

const FloskelnNeu = () => import("~/components/schule/kataloge/floskeln/FloskelnNeu.vue");

export class RouteFloskelnNeu extends RouteNode<any, RouteFloskeln> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskeln.neu", "neu", FloskelnNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Floskeln";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FloskelnNeuProps {
		return {
			manager: () => routeFloskeln.data.manager,
			add: routeFloskeln.data.add,
			schuljahr: api.abschnitt.schuljahr,
			schulform: api.schulform,
			goToDefaultView: routeFloskeln.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFloskelnNeu = new RouteFloskelnNeu();
