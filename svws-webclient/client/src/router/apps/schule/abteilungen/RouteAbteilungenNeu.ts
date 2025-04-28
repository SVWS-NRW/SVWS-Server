import type { RouteLocationNormalized } from "vue-router";
import type { AbteilungenNeuProps } from "~/components/schule/kataloge/abteilungen/SAbteilungenNeuProps";
import type { RouteAbteilungen } from "~/router/apps/schule/abteilungen/RouteAbteilungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeAbteilungen } from "~/router/apps/schule/abteilungen/RouteAbteilungen";

const SAbteilungenNeu = () => import("~/components/schule/kataloge/abteilungen/SAbteilungenNeu.vue");

export class RouteAbteilungenNeu extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_AENDERN ], "schule.abteilungen.neu", "neu", SAbteilungenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "abteilungen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): AbteilungenNeuProps {
		return {
			manager: () => routeAbteilungen.data.manager,
			goToDefaultView: routeAbteilungen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}
}

export const routeAbteilungenNeu = new RouteAbteilungenNeu();
