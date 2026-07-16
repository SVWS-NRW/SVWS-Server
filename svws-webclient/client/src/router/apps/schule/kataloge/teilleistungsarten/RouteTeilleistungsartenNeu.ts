import { RouteNode } from "~/router/RouteNode";
import { routeTeilleistungsarten, type RouteTeilleistungsarten } from "./RouteTeilleistungsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import type { RouteLocationNormalized } from "vue-router";
import type { TeilleistungsartenNeuProps } from "~/components/schule/kataloge/teilleistungsarten/TeilleistungsartenNeuProps";
import { RouteManager } from "~/router/RouteManager";


const TeilleistungsartenNeu = () =>
	import("~/components/schule/kataloge/teilleistungsarten/TeilleistungsartenNeu.vue");

export class RouteTeilleistungsartenNeu extends RouteNode<any, RouteTeilleistungsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.teilleistungsarten.neu", "neu", TeilleistungsartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Teilleistungsarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): TeilleistungsartenNeuProps {
		return {
			manager: () => routeTeilleistungsarten.data.manager,
			add: routeTeilleistungsarten.data.add,
			gotoDefaultView: routeTeilleistungsarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}
export const routeTeilleistungsartenNeu = new RouteTeilleistungsartenNeu();
