import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { type RouteSchuelerAbitur } from "~/router/apps/schueler/abitur/RouteSchuelerAbitur";

import type { SchuelerAbiturLeistungsuebersichtProps } from "~/components/schueler/abitur/SchuelerAbiturLeistungsuebersichtProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler } from "../RouteSchueler";

const SchuelerAbiturLeistungsuebersicht = () => import("~/components/schueler/abitur/SchuelerAbiturLeistungsuebersicht.vue");

export class RouteSchuelerAbiturLeistungsuebersicht extends RouteNode<any, RouteSchuelerAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN
		], "schueler.abitur.leistungsuebersicht", "leistungsuebersicht", SchuelerAbiturLeistungsuebersicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsübersicht";
		this.isHidden = (params?: RouteParams) => this.parent?.hidden(params) ?? false; // TODO automatically perform parent check in hidden method in RouteNode
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbiturLeistungsuebersichtProps {
		return {
			serverMode: api.mode,
			schule: api.schuleStammdaten,
		};
	}

}

export const routeSchuelerAbiturLeistungsuebersicht = new RouteSchuelerAbiturLeistungsuebersicht();

