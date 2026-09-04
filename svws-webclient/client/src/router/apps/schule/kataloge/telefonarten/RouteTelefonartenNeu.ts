import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { RouteTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import type { TelefonartenNeuProps } from "~/components/schule/kataloge/telefonarten/TelefonartenNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const TelefonartenNeu = () => import("~/components/schule/kataloge/telefonarten/TelefonartenNeu.vue");

export class RouteTelefonartenNeu extends RouteNode<any, RouteTelefonarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.telefonarten.neu", "neu", TelefonartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Telefonarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): TelefonartenNeuProps {
		return {
			manager: () => routeTelefonarten.data.manager,
			add: routeTelefonarten.data.add,
			gotoDefaultView: routeTelefonarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeTelefonartenNeu = new RouteTelefonartenNeu();
