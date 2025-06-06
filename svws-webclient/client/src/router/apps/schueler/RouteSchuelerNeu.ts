import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";

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

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): SchuelerNeuProps {
		return {
			schuelerListeManager: () => routeSchueler.data.manager,
			addSchueler: routeSchueler.data.addSchueler,
			patch: routeSchueler.data.patchSchuelerNeu,
			gotoDefaultView: routeSchueler.data.gotoDefaultView,
			mapSchulen: routeApp.data.mapSchulen,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			mapReligionen: routeApp.data.mapReligionen,
			mapFahrschuelerarten: routeApp.data.mapFahrschuelerarten,
			mapHaltestellen: routeApp.data.mapHaltestellen,
			mapKindergaerten: routeApp.data.mapKindergaerten,
			mapEinschulungsarten: routeApp.data.mapEinschulungsarten,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			schulform: api.schulform,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeSchuelerNeu = new RouteSchuelerNeu();
