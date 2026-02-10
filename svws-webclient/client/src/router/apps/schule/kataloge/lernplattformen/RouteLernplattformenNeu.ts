import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import { routeLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import type { LernplattformenNeuProps } from "~/components/schule/kataloge/lernplattformen/LernplattformenNeuProps";
import { api } from "~/router/Api";

const LernplattformenNeu = () =>
	import("~/components/schule/kataloge/lernplattformen/LernplattformenNeu.vue");

export class RouteLernplattformenNeu extends RouteNode<any, RouteLernplattformen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.lernplattformen.neu", "neu", LernplattformenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattformen Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): LernplattformenNeuProps {
		return {
			manager: () => routeLernplattformen.data.manager,
			add: routeLernplattformen.data.add,
			gotoDefaultView: routeLernplattformen.data.gotoDefaultView,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeLernplattformenNeu = new RouteLernplattformenNeu();
