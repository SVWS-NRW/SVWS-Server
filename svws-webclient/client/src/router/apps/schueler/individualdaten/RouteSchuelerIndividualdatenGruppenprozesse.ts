import type { DeveloperNotificationException } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";
import { type SchuelerIndividualdatenGruppenprozesseProps } from "~/components/schueler/individualdaten/SSchuelerIndividualdatenGruppenprozesseProps";
import { RouteManager } from "~/router/RouteManager";
import { RouteDataSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteDataSchuelerIndividualdaten";
import { routeError } from "~/router/error/RouteError";

const SSchuelerIndividualdatenGruppenprozesse = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdatenGruppenprozesse.vue");

export class RouteSchuelerIndividualdatenGruppenprozesse extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler.gruppenprozesse.daten", "gruppenprozesse/daten", SSchuelerIndividualdatenGruppenprozesse, new RouteDataSchuelerIndividualdaten());
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.propHandler = (props) => this.getProps(props);
		super.mode = ServerMode.DEV; // checkHidden() kann wieder entfernt werden, sobald der ServerMode für diese Route auf Stable gesetzt ist
		super.text = "Individualdaten";
		super.setCheckpoint = true;
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			if (api.mode === ServerMode.DEV)
				return false;
			return this.getRoute(params);
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async leaveBefore(from: RouteNode<any, any>, from_params: RouteParams): Promise<any> {
		this.data.pendingStateManager.resetPendingState();
		routeSchueler.data.pendingStateManagerRegistry.removeAllPendingStateManager();
	}

	public getProps(_: RouteLocationNormalized): SchuelerIndividualdatenGruppenprozesseProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schuelerListeManager: () => routeSchueler.data.manager,
			pendingStateManager: () => this.data.pendingStateManager,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			mapFahrschuelerarten: this.data.mapFahrschuelerarten,
			mapFoerderschwerpunkte: this.data.mapFoerderschwerpunkte,
			mapHaltestellen: this.data.mapHaltestellen,
			mapReligionen: this.data.mapReligionen,
			mapSchulen: this.data.mapSchulen,
			autofocus: routeSchueler.data.autofocus,
			patchMultiple: () => routeSchueler.data.patchMultiple(this.data.pendingStateManager),
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeSchuelerIndividualdatenGruppenprozesse = new RouteSchuelerIndividualdatenGruppenprozesse();

