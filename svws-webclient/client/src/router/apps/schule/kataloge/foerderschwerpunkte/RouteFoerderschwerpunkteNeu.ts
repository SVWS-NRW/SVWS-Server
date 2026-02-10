import type { RouteLocationNormalized } from "vue-router";
import type { FoerderschwerpunkteNeuProps } from "~/components/schule/kataloge/foerderschwerpunkte/FoerderschwerpunkteNeuProps";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";

const FoerderschwerpunkteNeu = () => import("~/components/schule/kataloge/foerderschwerpunkte/FoerderschwerpunkteNeu.vue");

export class RouteFoerderschwerpunkteNeu extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.foerderschwerpunkte.neu", "neu", FoerderschwerpunkteNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteNeuProps {
		return {
			manager: () => routeFoerderschwerpunkte.data.manager,
			add: routeFoerderschwerpunkte.data.addFoerderschwerpunkt,
			schulform: api.schulform,
			schuljahr: api.abschnitt.schuljahr,
			goToDefaultView: routeFoerderschwerpunkte.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFoerderschwerpunkteNeu = new RouteFoerderschwerpunkteNeu();
