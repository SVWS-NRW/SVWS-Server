import type { RouteLocationNormalized } from "vue-router";
import type { SportbefreiungenNeuProps } from "~/components/schule/schulbezogen/sportbefreiungen/SSportbefreiungenNeuProps";
import type{ RouteSportbefreiungen } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeSportbefreiungen } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungen";

const SSportbefreiungenNeu = () => import("~/components/schule/schulbezogen/sportbefreiungen/SSportbefreiungenNeu.vue");

export class RouteSportbefreiungenNeu extends RouteNode<any, RouteSportbefreiungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.sportbefreiungen.neu", "neu", SSportbefreiungenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Sportbefreiungen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SportbefreiungenNeuProps {
		return {
			manager: () => routeSportbefreiungen.data.manager,
			addSportbefreiung: routeSportbefreiungen.data.addSportbefreiung,
			goToDefaultView: routeSportbefreiungen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}
}

export const routeSportbefreiungenNeu = new RouteSportbefreiungenNeu();
