import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostAbitur, type RouteGostAbitur } from "~/router/apps/gost/abitur/RouteGostAbitur";

import type { GostAbiturPruefungsuebersichtProps } from "~/components/gost/abitur/GostAbiturPruefungsuebersichtProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";

const GostAbiturPruefungsuebersicht = () => import("~/components/gost/abitur/GostAbiturPruefungsuebersicht.vue");

export class RouteGostAbiturPruefungsuebersicht extends RouteNode<any, RouteGostAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "gost.abitur.pruefungsuebersicht", "pruefungsuebersicht", GostAbiturPruefungsuebersicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Prüfung";
		this.isHidden = (params?: RouteParams) => this.parent?.hidden(params) ?? false; // TODO automatically perform parent check in hidden method in RouteNode
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): GostAbiturPruefungsuebersichtProps {
		return {
			serverMode: api.mode,
			schule: api.schuleStammdaten,
			schuelerListe: routeGostAbitur.data.schuelerListe,
			managerMap: () => routeGostAbitur.data.managerAbiturMap,
			updateAbiturpruefungsdaten: routeGostAbitur.data.updateAbiturpruefungsdaten,
		};
	}

}

export const routeGostAbiturPruefungsuebersicht = new RouteGostAbiturPruefungsuebersicht();

