import type { HaltestellenNeuProps } from "~/components/schule/allgemein/haltestellen/SHaltestellenNeuProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteHaltestellen } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeHaltestellen } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellen";

const SHaltestellenNeu = () => import("~/components/schule/allgemein/haltestellen/SHaltestellenNeu.vue");

export class RouteHaltestellenNeu extends RouteNode<any, RouteHaltestellen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.haltestellen.neu", "neu", SHaltestellenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Haltestellen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): HaltestellenNeuProps {
		return {
			manager: () => routeHaltestellen.data.manager,
			addHaltestelle: routeHaltestellen.data.addHaltestelle,
			goToDefaultView: routeHaltestellen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}
}

export const routeHaltestellenNeu = new RouteHaltestellenNeu();
