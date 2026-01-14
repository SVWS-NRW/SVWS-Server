import type { RouteLocationNormalized } from "vue-router";
import type { EntlassgruendeNeuProps } from "~/components/schule/kataloge/entlassgruende/EntlassgruendeNeuProps";
import type { RouteEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruende";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruende";
import { api } from "~/router/Api";

const EntlassgruendeNeu = () => import("~/components/schule/kataloge/entlassgruende/EntlassgruendeNeu.vue");

export class RouteEntlassgruendeNeu extends RouteNode<any, RouteEntlassgruende> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.entlassgruende.neu", "neu", EntlassgruendeNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Entlassgründe";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): EntlassgruendeNeuProps {
		return {
			manager: () => routeEntlassgruende.data.manager,
			add: routeEntlassgruende.data.addEntlassgrund,
			goToDefaultView: routeEntlassgruende.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeEntlassgruendeNeu = new RouteEntlassgruendeNeu();
