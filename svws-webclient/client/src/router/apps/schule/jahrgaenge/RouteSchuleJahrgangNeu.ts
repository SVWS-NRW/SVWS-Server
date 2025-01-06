import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuleJahrgaenge } from "./RouteSchuleJahrgaenge";
import type { RouteSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";
import type { SchuleJahrgangNeuProps } from "~/components/schule/jahrgaenge/SSchuleJahrgangNeuProps";

const SSchuleJahrgangNeu = () => import("~/components/schule/jahrgaenge/SSchuleJahrgangNeu.vue");

export class RouteSchuleJahrgangNeu extends RouteNode<any, RouteSchuleJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.jahrgaenge.neu", "neu", SSchuleJahrgangNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchuleJahrgangNeuProps {
		return {
			jahrgangListeManager: () => routeSchuleJahrgaenge.data.manager,
			add: routeSchuleJahrgaenge.data.add,
			gotoDefaultView: routeSchuleJahrgaenge.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeSchuleJahrgangNeu = new RouteSchuleJahrgangNeu();
