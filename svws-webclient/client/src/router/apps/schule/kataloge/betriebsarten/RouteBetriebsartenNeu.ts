import { RouteNode } from "~/router/RouteNode";
import { routeBetriebsarten, type RouteBetriebsarten } from "./RouteBetriebsarten";
import type { RouteLocationNormalized } from "vue-router";
import type { BetriebsartenNeuProps } from "~/components/schule/kataloge/betriebsarten/BetriebsartenNeuProps";
import { RouteManager } from "~/router/RouteManager";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";


const BetriebsartenNeu = () => import("~/components/schule/kataloge/betriebsarten/BetriebsartenNeu.vue");

export class RouteBetriebsartenNeu extends RouteNode<any, RouteBetriebsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.betriebsarten.neu", "neu", BetriebsartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betriebsarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): BetriebsartenNeuProps {
		return {
			manager: () => routeBetriebsarten.data.manager,
			add: routeBetriebsarten.data.add,
			gotoDefaultView: routeBetriebsarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}
export const routeBetriebsartenNeu = new RouteBetriebsartenNeu();
