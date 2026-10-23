import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { SchulinterneFunktionenNeuProps } from "~/components/schule/kataloge/schulinternefunktionen/SchulinterneFunktionenNeuProps";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteSchulinterneFunktionen } from "./RouteSchulinterneFunktionen";
import { routeSchulinterneFunktionen } from "./RouteSchulinterneFunktionen";

const SchulinterneFunktionenNeu = () => import("~/components/schule/kataloge/schulinternefunktionen/SchulinterneFunktionenNeu.vue");

export class RouteSchulinterneFunktionenNeu extends RouteNode<any, RouteSchulinterneFunktionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulinternefunktionen.neu", "neu", SchulinterneFunktionenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulinterne Funktionen";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SchulinterneFunktionenNeuProps {
		return {
			manager: () => routeSchulinterneFunktionen.data.manager,
			add: routeSchulinterneFunktionen.data.add,
			goToDefaultView: routeSchulinterneFunktionen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeSchulinterneFunktionenNeu = new RouteSchulinterneFunktionenNeu();
