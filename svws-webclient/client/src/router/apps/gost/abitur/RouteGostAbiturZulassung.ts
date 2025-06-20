import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostAbitur, type RouteGostAbitur } from "~/router/apps/gost/abitur/RouteGostAbitur";

import type { GostAbiturZulassungProps } from "~/components/gost/abitur/GostAbiturZulassungProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";

const GostAbiturZulassung = () => import("~/components/gost/abitur/GostAbiturZulassung.vue");

export class RouteGostAbiturZulassung extends RouteNode<any, RouteGostAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "gost.abitur.zulassung", "zulassung", GostAbiturZulassung);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zulassung";
		this.isHidden = (params?: RouteParams) => this.parent?.hidden(params) ?? false; // TODO automatically perform parent check in hidden method in RouteNode
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): GostAbiturZulassungProps {
		return {
			serverMode: api.mode,
			schule: api.schuleStammdaten,
			schuelerListe: routeGostAbitur.data.schuelerListe,
			managerLaufbahnplanungMap: () => routeGostAbitur.data.managerLaufbahnplanungMap,
			ergebnisBelegpruefungMap: () => routeGostAbitur.data.ergebnisBelegpruefungMap,
			managerAbiturMap: () => routeGostAbitur.data.managerAbiturMap,
			copyAbiturdatenAusLeistungsdaten: routeGostAbitur.data.copyAbiturdatenAusLeistungsdaten,
			updateAbiturpruefungsdaten: routeGostAbitur.data.updateAbiturpruefungsdaten,
		};
	}

}

export const routeGostAbiturZulassung = new RouteGostAbiturZulassung();

