import type { RouteLocationNormalized } from "vue-router";
import type { KindergaertenNeuProps } from "~/components/schule/kataloge/kindergaerten/KindergaertenNeuProps";
import type { RouteKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";

const KindergaertenNeu = () => import("~/components/schule/kataloge/kindergaerten/KindergaertenNeu.vue");

export class RouteKindergaertenNeu extends RouteNode<any, RouteKindergaerten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.kindergaerten.neu", "neu", KindergaertenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kindergaerten";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): KindergaertenNeuProps {
		return {
			manager: () => routeKindergaerten.data.manager,
			add: routeKindergaerten.data.add,
			goToDefaultView: routeKindergaerten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKindergaertenNeu = new RouteKindergaertenNeu();
