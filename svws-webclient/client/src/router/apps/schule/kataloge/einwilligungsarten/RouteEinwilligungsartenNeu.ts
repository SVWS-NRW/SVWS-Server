import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import { routeEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import type { EinwilligungsartenNeuProps } from "~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenNeuProps";
import { api } from "~/router/Api";

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
			schulform: api.schulform,
			schuljahr: api.abschnitt.schuljahr,
			add: routeEinwilligungsarten.data.add,
			benutzerKompetenzen: api.benutzerKompetenzen,
			gotoDefaultView: routeEinwilligungsarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeEinwilligungsartenNeu = new RouteEinwilligungsartenNeu();
