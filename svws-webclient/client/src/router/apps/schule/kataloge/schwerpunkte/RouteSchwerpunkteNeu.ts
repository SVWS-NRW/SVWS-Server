import { RouteNode } from "~/router/RouteNode";
import { routeSchwerpunkte, type RouteSchwerpunkte } from "./RouteSchwerpunkte";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import type { RouteLocationNormalized } from "vue-router";
import type { SchwerpunkteNeuProps } from "~/components/schule/kataloge/schwerpunkte/SchwerpunkteNeuProps";
import { RouteManager } from "~/router/RouteManager";


const SchwerpunkteNeu = () => import("~/components/schule/kataloge/schwerpunkte/SchwerpunkteNeu.vue");

export class RouteSchwerpunkteNeu extends RouteNode<any, RouteSchwerpunkte> {
	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB, Schulform.R], [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schwerpunkte.neu", "neu", SchwerpunkteNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schwerpunkte Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SchwerpunkteNeuProps {
		return {
			manager: () => routeSchwerpunkte.data.manager,
			add: routeSchwerpunkte.data.add,
			gotoDefaultView: routeSchwerpunkte.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}
export const routeSchwerpunkteNeu = new RouteSchwerpunkteNeu();
