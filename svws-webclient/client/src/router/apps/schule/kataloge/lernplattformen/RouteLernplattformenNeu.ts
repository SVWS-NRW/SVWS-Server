import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { RouteLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import { routeLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import type { LernplattformenNeuProps } from "~/components/schule/kataloge/lernplattformen/LernplattformenNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const LernplattformenNeu = () =>
	import("~/components/schule/kataloge/lernplattformen/LernplattformenNeu.vue");

export class RouteLernplattformenNeu extends RouteNode<any, RouteLernplattformen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.lernplattformen.neu", "neu", LernplattformenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattformen Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): LernplattformenNeuProps {
		return {
			manager: () => routeLernplattformen.data.manager,
			add: routeLernplattformen.data.add,
			gotoDefaultView: routeLernplattformen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeLernplattformenNeu = new RouteLernplattformenNeu();
