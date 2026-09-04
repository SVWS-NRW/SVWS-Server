import type { RouteLocationNormalized } from "vue-router";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import type { RouteFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { routeFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import type { FloskelgruppenNeuProps } from "~/components/schule/kataloge/floskelgruppen/FloskelgruppenNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const FloskelgruppenNeu = () => import("~/components/schule/kataloge/floskelgruppen/FloskelgruppenNeu.vue");

export class RouteFloskelgruppenNeu extends RouteNode<any, RouteFloskelgruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskelgruppen.neu", "neu", FloskelgruppenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Floskelgruppen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FloskelgruppenNeuProps {
		return {
			manager: () => routeFloskelgruppen.data.manager,
			add: routeFloskelgruppen.data.add,
			goToDefaultView: routeFloskelgruppen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFloskelgruppenNeu = new RouteFloskelgruppenNeu();
