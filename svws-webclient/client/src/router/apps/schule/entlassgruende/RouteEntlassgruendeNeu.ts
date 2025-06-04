import type { RouteLocationNormalized } from "vue-router";
import type { EntlassgruendeNeuProps } from "~/components/schule/kataloge/entlassgruende/SEntlassgruendeNeuProps";
import type { RouteEntlassgruende } from "~/router/apps/schule/entlassgruende/RouteEntlassgruende";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeEntlassgruende } from "~/router/apps/schule/entlassgruende/RouteEntlassgruende";

const SEntlassgruendeNeu = () => import("~/components/schule/kataloge/entlassgruende/SEntlassgruendeNeu.vue");

export class RouteEntlassgruendeNeu extends RouteNode<any, RouteEntlassgruende> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.entlassgruende.neu", "neu", SEntlassgruendeNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Entlassgründe";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): EntlassgruendeNeuProps {
		return {
			manager: () => routeEntlassgruende.data.manager,
			goToDefaultView: routeEntlassgruende.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}

}

export const routeEntlassgruendeNeu = new RouteEntlassgruendeNeu();
