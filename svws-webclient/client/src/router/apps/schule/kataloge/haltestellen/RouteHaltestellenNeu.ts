import type { HaltestellenNeuProps } from "~/components/schule/kataloge/haltestellen/HaltestellenNeuProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const HaltestellenNeu = () => import("~/components/schule/kataloge/haltestellen/HaltestellenNeu.vue");

export class RouteHaltestellenNeu extends RouteNode<any, RouteHaltestellen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.haltestellen.neu", "neu", HaltestellenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Haltestellen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): HaltestellenNeuProps {
		return {
			manager: () => routeHaltestellen.data.manager,
			add: routeHaltestellen.data.add,
			goToDefaultView: routeHaltestellen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeHaltestellenNeu = new RouteHaltestellenNeu();
