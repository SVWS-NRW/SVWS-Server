import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import type { TelefonartenNeuProps } from "~/components/schule/kataloge/telefonarten/TelefonartenNeuProps";
import { api } from "~/router/Api";

const TelefonartenNeu = () => import("~/components/schule/kataloge/telefonarten/TelefonartenNeu.vue");

export class RouteTelefonartenNeu extends RouteNode<any, RouteTelefonarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.telefonarten.neu", "neu", TelefonartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Telefonarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): TelefonartenNeuProps {
		return {
			manager: () => routeTelefonarten.data.manager,
			add: routeTelefonarten.data.add,
			gotoDefaultView: routeTelefonarten.data.gotoDefaultView,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeTelefonartenNeu = new RouteTelefonartenNeu();
