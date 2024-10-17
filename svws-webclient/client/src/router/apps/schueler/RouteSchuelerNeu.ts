import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";

const SSchuelerNeu = () => import("~/components/schueler/SSchuelerNeu.vue");

export class RouteSchuelerNeu extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN ], "schueler.neu", "neu", SSchuelerNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchuelerNeuProps {
		return {
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeSchuelerNeu = new RouteSchuelerNeu();
