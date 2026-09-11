import type { RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { routeUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

const SUvPlanungsabschnittNeu = () => import("~/components/unterrichtsverteilung/SUvPlanungsabschnittNeu.vue");

export class RouteUvPlanungsabschnittNeu extends RouteNode<any, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN], "uv.planungsabschnitt.neu", "neu", SUvPlanungsabschnittNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Planungsabschnitt Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps() {
		const parent = this.parent;
		if (parent === undefined) {
			throw new DeveloperNotificationException('Die UV-Neuanlage-Route hat keine Elternroute.');
		}
		return {
			gotoDefaultView: routeUv.data.gotoDefaultView,
			manager: () => parent.data.manager,
			addAsCopy: parent.data.addAsCopy,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeUvPlanungsabschnittNeu = new RouteUvPlanungsabschnittNeu();
