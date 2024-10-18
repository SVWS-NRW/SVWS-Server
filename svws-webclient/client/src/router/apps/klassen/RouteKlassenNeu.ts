import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeApp } from "../RouteApp";
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

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KlassenNeuProps {
		return {
			klassenListeManager: () => routeKlassen.data.klassenListeManager,
			schulform: api.schulform,
			mapKlassenVorigerAbschnitt: () => routeKlassen.data.mapKlassenVorigerAbschnitt,
			mapKlassenFolgenderAbschnitt: () => routeKlassen.data.mapKlassenFolgenderAbschnitt,
			add: routeKlassen.data.add,
			gotoEintrag: routeKlassen.data.gotoEintrag,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeKlassenNeu = new RouteKlassenNeu();
