import type { RouteLocationNormalized } from "vue-router";
import type { AbteilungenNeuProps } from "~/components/schule/kataloge/abteilungen/AbteilungenNeuProps";
import type { RouteAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const AbteilungenNeu = () => import("~/components/schule/kataloge/abteilungen/AbteilungenNeu.vue");

export class RouteAbteilungenNeu extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_AENDERN], "schule.abteilungen.neu", "neu", AbteilungenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "abteilungen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): AbteilungenNeuProps {
		return {
			manager: () => routeAbteilungen.data.manager,
			add: routeAbteilungen.data.add,
			goToDefaultView: routeAbteilungen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeAbteilungenNeu = new RouteAbteilungenNeu();
