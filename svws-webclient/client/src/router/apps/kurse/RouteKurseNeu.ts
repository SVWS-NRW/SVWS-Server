import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { KurseNeuProps } from "~/components/kurse/SKurseNeuProps";
import { type RouteKurse, routeKurse } from "./RouteKurse";

const SKurseNeu = () => import("~/components/kurse/SKurseNeu.vue");

export class RouteKurseNeu extends RouteNode<any, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kurse.neu", "neu", SKurseNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurs Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): KurseNeuProps {
		return {
			kurseListeManager: () => routeKurse.data.kursListeManager,
			schulform: api.schulform,
			add: routeKurse.data.add,
			gotoDefaultView: routeKurse.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKurseNeu = new RouteKurseNeu();