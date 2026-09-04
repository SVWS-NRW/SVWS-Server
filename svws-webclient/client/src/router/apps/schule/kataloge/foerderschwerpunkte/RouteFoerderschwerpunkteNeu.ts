import type { RouteLocationNormalized } from "vue-router";
import type { FoerderschwerpunkteNeuProps } from "~/components/schule/kataloge/foerderschwerpunkte/FoerderschwerpunkteNeuProps";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const FoerderschwerpunkteNeu = () => import("~/components/schule/kataloge/foerderschwerpunkte/FoerderschwerpunkteNeu.vue");

export class RouteFoerderschwerpunkteNeu extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.foerderschwerpunkte.neu", "neu", FoerderschwerpunkteNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteNeuProps {
		return {
			manager: () => routeFoerderschwerpunkte.data.manager,
			add: routeFoerderschwerpunkte.data.addFoerderschwerpunkt,
			goToDefaultView: routeFoerderschwerpunkte.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFoerderschwerpunkteNeu = new RouteFoerderschwerpunkteNeu();
