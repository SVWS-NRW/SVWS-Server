import type { RouteLocationNormalized, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import { type SchuelerIndividualdatenGruppenprozesseProps } from "~/components/schueler/individualdaten/SchuelerIndividualdatenGruppenprozesseProps";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteDataSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteDataSchuelerIndividualdaten";
import { type RouteSchueler, routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeError } from "~/router/error/RouteError";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { serverStateImpl } from "~/states/ServerStateImpl";

const SchuelerIndividualdatenGruppenprozesse = () => import("~/components/schueler/individualdaten/SchuelerIndividualdatenGruppenprozesse.vue");

export class RouteSchuelerIndividualdatenGruppenprozesse extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler.gruppenprozesse.daten", "gruppenprozesse/daten", SchuelerIndividualdatenGruppenprozesse, new RouteDataSchuelerIndividualdaten());
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.propHandler = (props) => this.getProps(props);
		super.mode = ServerMode.DEV; // checkHidden() kann wieder entfernt werden, sobald der ServerMode für diese Route auf Stable gesetzt ist
		super.text = "Individualdaten";
		super.setCheckpoint = true;
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			if (serverStateImpl.hasDev) {
				return false;
			}
			return this.getRoute(params);
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async leaveBefore(from: RouteNode<any, any>, from_params: RouteParams): Promise<any> {
		this.data.pendingStateManager.resetPendingState();
		routeSchueler.data.pendingStateManagerRegistry.removeAllPendingStateManager();
	}

	public getProps(_: RouteLocationNormalized): SchuelerIndividualdatenGruppenprozesseProps {
		return {
			pendingStateManager: () => this.data.pendingStateManager,
			foerderschwerpunkteById: routeApp.cache.kataloge.foerderschwerpunkteById,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeSchuelerIndividualdatenGruppenprozesse = new RouteSchuelerIndividualdatenGruppenprozesse();
