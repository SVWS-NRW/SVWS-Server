import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerAbitur, type RouteSchuelerAbitur } from "~/router/apps/schueler/abitur/RouteSchuelerAbitur";

import type { SchuelerAbiturPruefungsuebersichtProps } from "~/components/schueler/abitur/SchuelerAbiturPruefungsuebersichtProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";

const SchuelerAbiturPruefungsuebersicht = () => import("~/components/schueler/abitur/SchuelerAbiturPruefungsuebersicht.vue");

export class RouteSchuelerAbiturPruefungsuebersicht extends RouteNode<any, RouteSchuelerAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "schueler.abitur.pruefungsuebersicht", "pruefungsuebersicht", SchuelerAbiturPruefungsuebersicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Prüfung";
		this.isHidden = (params?: RouteParams) => this.parent?.hidden(params) ?? false; // TODO automatically perform parent check in hidden method in RouteNode
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbiturPruefungsuebersichtProps {
		return {
			serverMode: api.mode,
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerAbitur.data.managerAbitur,
		};
	}

}

export const routeSchuelerAbiturPruefungsuebersicht = new RouteSchuelerAbiturPruefungsuebersicht();

