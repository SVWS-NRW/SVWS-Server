import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import type { DeveloperNotificationException, SchuelerStammdatenNeu } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";
import { routeError } from "~/router/error/RouteError";
import { routeSchuelerNeuSchnelleingabe } from "~/router/apps/schueler/RouteSchuelerNeuSchnelleingabe";

const SSchuelerNeu = () => import("~/components/schueler/SSchuelerNeu.vue");

export class RouteSchuelerNeu extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN], "schueler.neu", "neu", SSchuelerNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler Neu";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
		super.setCheckpoint = true;
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params !== undefined) ? RouteNode.getIntParams(params, ["id"]) : { id: undefined };
			if (!routeSchueler.data.manager.hasDaten()) {
				return false;
			}
			if (api.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
				return false;
			}
			return routeSchueler.getRouteDefaultChild({ id });
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	private initialeSchuelerDaten: SchuelerStammdatenNeu | null = null;

	public async getSchuelerDaten(idSchueler: number): Promise<void> {
		const auswahl = routeSchueler.data.manager.liste.get(idSchueler);
		this.initialeSchuelerDaten = await routeSchuelerNeuSchnelleingabe.data.ladeInitialeDatenFuerWeiterenSchueler(auswahl) ?? null;
	}

	public clearInitialeSchuelerDaten(): void {
		this.initialeSchuelerDaten = null;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (from === routeSchuelerNeuSchnelleingabe) {
			const idSchueler = routeSchueler.data.manager.daten().id;
			await this.getSchuelerDaten(idSchueler);
		} else {
			this.clearInitialeSchuelerDaten();
		}
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): SchuelerNeuProps {
		return {
			schuelerListeManager: () => routeSchueler.data.manager,
			addSchueler: routeSchueler.data.addSchueler,
			getSchuelerKlassenFuerAbschnitt: routeSchueler.data.getSchuelerKlassenFuerAbschnitt,
			patchSchuelerSchulbesuchdaten: routeSchueler.data.patchSchuelerSchulbesuchdaten,
			mapKindergaerten: routeApp.data.mapKindergaerten,
			mapEinschulungsarten: routeApp.data.mapEinschulungsarten,
			initialeSchuelerDaten: () => this.initialeSchuelerDaten,
			gotoSchnelleingabeView: routeSchueler.data.gotoSchnelleingabeView,
			gotoDefaultView: routeSchueler.data.gotoDefaultView,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeSchuelerNeu = new RouteSchuelerNeu();
