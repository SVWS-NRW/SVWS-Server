import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import type { RouteFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { routeFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import type { FloskelgruppenNeuProps } from "~/components/schule/kataloge/floskelgruppen/FloskelgruppenNeuProps";

const FloskelgruppenNeu = () => import("~/components/schule/kataloge/floskelgruppen/FloskelgruppenNeu.vue");

export class RouteFloskelgruppenNeu extends RouteNode<any, RouteFloskelgruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskelgruppen.neu", "neu", FloskelgruppenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Floskelgruppen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FloskelgruppenNeuProps {
		return {
			manager: () => routeFloskelgruppen.data.manager,
			add: routeFloskelgruppen.data.add,
			schuljahr: api.abschnitt.schuljahr,
			schulform: api.schulform,
			goToDefaultView: routeFloskelgruppen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFloskelgruppenNeu = new RouteFloskelgruppenNeu();
