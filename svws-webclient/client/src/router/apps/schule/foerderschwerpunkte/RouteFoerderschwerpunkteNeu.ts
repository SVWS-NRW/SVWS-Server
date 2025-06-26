import type { RouteLocationNormalized } from "vue-router";
import type { FoerderschwerpunkteNeuProps } from "~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteNeuProps";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/foerderschwerpunkte/RouteFoerderschwerpunkte";

const SFoerderschwerpunkteNeu = () => import("~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteNeu.vue");

export class RouteFoerderschwerpunkteNeu extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.foerderschwerpunkte.neu", "neu", SFoerderschwerpunkteNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteNeuProps {
		return {
			manager: () => routeFoerderschwerpunkte.data.manager,
			addFoerderschwerpunkt: routeFoerderschwerpunkte.data.addFoerderschwerpunkt,
			goToDefaultView: routeFoerderschwerpunkte.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}
}

export const routeFoerderschwerpunkteNeu = new RouteFoerderschwerpunkteNeu();
