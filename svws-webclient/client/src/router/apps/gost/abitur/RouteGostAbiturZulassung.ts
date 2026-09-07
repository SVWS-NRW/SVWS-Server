import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { GostAbiturZulassungProps } from "~/components/gost/abitur/GostAbiturZulassungProps";
import { type RouteGostAbitur, routeGostAbitur } from "~/router/apps/gost/abitur/RouteGostAbitur";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";

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

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): GostAbiturZulassungProps {
		return {
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

