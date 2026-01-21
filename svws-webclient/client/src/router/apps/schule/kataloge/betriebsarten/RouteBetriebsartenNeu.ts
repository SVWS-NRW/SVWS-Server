import { RouteNode } from "~/router/RouteNode";
import { routeBetriebsarten, type RouteBetriebsarten } from "./RouteBetriebsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import type { RouteLocationNormalized } from "vue-router";
import type { BetriebsartenNeuProps } from "~/components/schule/kataloge/betriebsarten/BetriebsartenNeuProps";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";


const BetriebsartenNeu = () => import("~/components/schule/kataloge/betriebsarten/BetriebsartenNeu.vue");

export class RouteBetriebsartenNeu extends RouteNode<any, RouteBetriebsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.betriebsarten.neu", "neu", BetriebsartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betriebsarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): BetriebsartenNeuProps {
		return {
			manager: () => routeBetriebsarten.data.manager,
			add: routeBetriebsarten.data.add,
			gotoDefaultView: routeBetriebsarten.data.gotoDefaultView,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}
export const routeBetriebsartenNeu = new RouteBetriebsartenNeu();