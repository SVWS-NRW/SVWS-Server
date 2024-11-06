import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "./RouteSchuleFaecher";
import type { SchuleFachNeuProps } from "~/components/schule/faecher/SSchuleFachNeuProps";

const SSchuleFachNeu = () => import("~/components/schule/faecher/SSchuleFachNeu.vue");

export class RouteSchuleFachNeu extends RouteNode<any, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.faecher.neu", "neu", SSchuleFachNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchuleFachNeuProps {
		return {
			fachListeManager: () => routeSchuleFaecher.data.fachListeManager,
			add: routeSchuleFaecher.data.add,
			gotoDefaultView: routeSchuleFaecher.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeSchuleFachNeu = new RouteSchuleFachNeu();
