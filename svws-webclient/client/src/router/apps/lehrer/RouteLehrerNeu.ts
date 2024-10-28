import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../RouteApp";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { LehrerNeuProps } from "~/components/lehrer/SLehrerNeuProps";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";

const SLehrerNeu = () => import("~/components/lehrer/SLehrerNeu.vue");

export class RouteLehrerNeu extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN ], "lehrer.neu", "neu", SLehrerNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrer Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): LehrerNeuProps {
		return {
			lehrerListeManager: () => routeLehrer.data.lehrerListeManager,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeLehrerNeu = new RouteLehrerNeu();