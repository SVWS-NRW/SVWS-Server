import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { EinwilligungsartenNeuProps } from "~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenNeuProps";
import type { RouteEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import { routeEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

const EinwilligungsartenNeu = () => import("~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenNeu.vue");

export class RouteEinwilligungsartenNeu extends RouteNode<any, RouteEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.einwilligungsarten.neu", "neu", EinwilligungsartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsart Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): EinwilligungsartenNeuProps {
		return {
			manager: () => routeEinwilligungsarten.data.manager,
			add: routeEinwilligungsarten.data.add,
			gotoDefaultView: routeEinwilligungsarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeEinwilligungsartenNeu = new RouteEinwilligungsartenNeu();
