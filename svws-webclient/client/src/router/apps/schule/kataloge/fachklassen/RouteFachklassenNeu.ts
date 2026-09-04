import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { RouteFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassen";
import { routeFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassen";
import type { FachklassenNeuProps } from "~/components/schule/kataloge/fachklassen/FachklassenNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";


const FachklassenNeu = () => import("~/components/schule/kataloge/fachklassen/FachklassenNeu.vue");

export class RouteFachklassenNeu extends RouteNode<any, RouteFachklassen> {
	public constructor() {
		super([Schulform.BK, Schulform.SB],
			[BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.fachklassen.neu",
			"neu",
			FachklassenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Fachklasse Neu";
		super.setCheckpoint = true;
	}

	public getProps(): FachklassenNeuProps {
		return {
			manager: () => routeFachklassen.data.manager,
			add: routeFachklassen.data.add,
			gotoDefaultView: routeFachklassen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}
export const routeFachklassenNeu = new RouteFachklassenNeu();
