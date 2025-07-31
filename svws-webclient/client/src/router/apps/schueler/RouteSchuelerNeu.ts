import type {RouteLocationNormalized, RouteParams, RouteParamsRawGeneric} from "vue-router";

import {BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode} from "@core";

import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";
import {routeError} from "~/router/error/RouteError";

const SSchuelerNeu = () => import("~/components/schueler/SSchuelerNeu.vue");

export class RouteSchuelerNeu extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN ], "schueler.neu", "neu", SSchuelerNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler Neu";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		super.setCheckpoint = true;
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params !== undefined) ? RouteNode.getIntParams(params, ["id"]) : {id: undefined};
			if (!routeSchueler.data.manager.hasDaten())
				return false;
			if (api.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN))
				return false;
			return routeSchueler.getRouteDefaultChild({ id });
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
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
			mapErzieherarten: routeApp.data.mapErzieherarten,
			mapTelefonArten: routeApp.data.mapTelefonArten,
			getListSchuelerErziehereintraege: () => routeSchueler.data.listSchuelerErziehereintraege,
			addSchuelerErziehereintrag: routeSchueler.data.addSchuelerErziehereintrag,
			patchSchuelerErziehereintrag: routeSchueler.data.patchSchuelerErziehereintrag,
			patchSchuelerErzieherAnPosition: routeSchueler.data.patchSchuelerErzieherAnPosition,
			deleteSchuelerErziehereintrage: routeSchueler.data.deleteSchuelerErziehereintrage,
			getListSchuelerTelefoneintraege: () => routeSchueler.data.listSchuelerTelefoneintraege,
			addSchuelerTelefoneintrag: routeSchueler.data.addSchuelerTelefoneintrag,
			patchSchuelerTelefoneintrag: routeSchueler.data.patchSchuelerTelefoneintrag,
			deleteSchuelerTelefoneintrage: routeSchueler.data.deleteSchuelerTelefoneintrage,
			patchSchuelerKindergarten: routeSchueler.data.patchSchuelerKindergarten,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeSchuelerNeu = new RouteSchuelerNeu();
