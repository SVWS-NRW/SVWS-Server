import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import type { RouteLeitungsfunktionen } from "./RouteLeitungsfunktionen";
import { routeLeitungsfunktionen } from "./RouteLeitungsfunktionen";
import type { LeitungsfunktionenNeuProps } from "~/components/schule/kataloge/leitungsfunktionen/LeitungsfunktionenNeuProps";

const LeitungsfunktionenNeu = () => import("~/components/schule/kataloge/leitungsfunktionen/LeitungsfunktionenNeu.vue");

export class RouteLeitungsfunktionenNeu extends RouteNode<any, RouteLeitungsfunktionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.leitungsfunktionen.neu", "neu", LeitungsfunktionenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leitungsfunktionen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): LeitungsfunktionenNeuProps {
		return {
			manager: () => routeLeitungsfunktionen.data.manager,
			add: routeLeitungsfunktionen.data.add,
			goToDefaultView: routeLeitungsfunktionen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeLeitungsfunktionenNeu = new RouteLeitungsfunktionenNeu();
