import type { RouteLocationNormalized } from "vue-router";
import type { MerkmaleNeuProps } from "~/components/schule/schulbezogen/merkmale/SMerkmaleNeuProps";
import type { RouteMerkmale } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmale";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeMerkmale } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmale";

const SMerkmaleNeu = () => import("~/components/schule/schulbezogen/merkmale/SMerkmaleNeu.vue");

export class RouteMerkmaleNeu extends RouteNode<any, RouteMerkmale> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.merkmale.neu", "neu", SMerkmaleNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Merkmale";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): MerkmaleNeuProps {
		return {
			manager: () => routeMerkmale.data.manager,
			addMerkmal: routeMerkmale.data.addMerkmal,
			goToDefaultView: routeMerkmale.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}
}

export const routeMerkmaleNeu = new RouteMerkmaleNeu();
