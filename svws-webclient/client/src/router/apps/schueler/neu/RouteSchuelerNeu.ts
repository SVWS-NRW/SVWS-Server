import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerNeuProps } from "~/components/schueler/neuanlage/SchuelerNeuProps";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { api } from "~/router/Api";
import { RouteDataSchuelerNeu } from "~/router/apps/schueler/neu/RouteDataSchuelerNeu";

const SchuelerNeu = () => import("~/components/schueler/neuanlage/SchuelerNeu.vue");

export class RouteSchuelerNeu extends RouteNode<RouteDataSchuelerNeu, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN], "schueler.neu", "neu", SchuelerNeu, new RouteDataSchuelerNeu());
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler Neu";
		super.setCheckpoint = true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeDaten();
	}

	public getProps(to: RouteLocationNormalized): SchuelerNeuProps {
		return {
			manager: () => this.data.manager,
			gotoDefaultView: routeSchueler.data.gotoDefaultView,
			add: routeSchueler.data.add,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			gotToSchnelleingabe: this.data.goToSchnelleingabe,
		};
	}

}

export const routeSchuelerNeu = new RouteSchuelerNeu();
