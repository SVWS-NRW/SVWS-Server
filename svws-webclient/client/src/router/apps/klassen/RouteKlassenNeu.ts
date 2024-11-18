import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { api } from "~/router/Api";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { KlassenNeuProps } from "~/components/klassen/SKlassenNeuProps";

const SKlassenNeu = () => import("~/components/klassen/SKlassenNeu.vue");

export class RouteKlassenNeu extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN ], "klassen.neu", "neu", SKlassenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klasse Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): KlassenNeuProps {
		return {
			manager: () => routeKlassen.data.manager,
			schulform: api.schulform,
			mapKlassenVorigerAbschnitt: () => routeKlassen.data.mapKlassenVorigerAbschnitt,
			mapKlassenFolgenderAbschnitt: () => routeKlassen.data.mapKlassenFolgenderAbschnitt,
			add: routeKlassen.data.add,
			gotoDefaultView: routeKlassen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeKlassenNeu = new RouteKlassenNeu();
