import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { KurseNeuProps } from "~/components/kurse/SKurseNeuProps";
import { type RouteKurse, routeKurse } from "./RouteKurse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const SKurseNeu = () => import("~/components/kurse/SKurseNeu.vue");

export class RouteKurseNeu extends RouteNode<any, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN], "kurse.neu", "neu", SKurseNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurs Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): KurseNeuProps {
		return {
			manager: () => routeKurse.data.manager,
			add: routeKurse.data.add,
			gotoDefaultView: routeKurse.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			goToDefaultView: routeKurse.data.gotoDefaultView,
		};
	}
}

export const routeKurseNeu = new RouteKurseNeu();
