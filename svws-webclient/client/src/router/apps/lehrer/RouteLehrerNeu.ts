import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import type { LehrerNeuProps } from "~/components/lehrer/LehrerNeuProps";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";

const LehrerNeu = () => import("~/components/lehrer/LehrerNeu.vue");

export class RouteLehrerNeu extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_AENDERN], "lehrer.neu", "neu", LehrerNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrer Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): LehrerNeuProps {
		return {
			lehrerListeManager: () => routeLehrer.data.manager,
			add: routeLehrer.data.add,
			gotoDefaultView: routeLehrer.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeLehrerNeu = new RouteLehrerNeu();
