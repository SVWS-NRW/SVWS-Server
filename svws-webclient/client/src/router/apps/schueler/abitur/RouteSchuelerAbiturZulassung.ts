import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerAbitur, type RouteSchuelerAbitur } from "~/router/apps/schueler/abitur/RouteSchuelerAbitur";

import type { SchuelerAbiturZulassungProps } from "~/components/schueler/abitur/SchuelerAbiturZulassungProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";

const SchuelerAbiturZulassung = () => import("~/components/schueler/abitur/SchuelerAbiturZulassung.vue");

export class RouteSchuelerAbiturZulassung extends RouteNode<any, RouteSchuelerAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "schueler.abitur.zulassung", "zulassung", SchuelerAbiturZulassung);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zulassung";
		this.isHidden = (params?: RouteParams) => this.parent?.hidden(params) ?? false; // TODO automatically perform parent check in hidden method in RouteNode
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbiturZulassungProps {
		return {
			serverMode: api.mode,
			schule: api.schuleStammdaten,
			managerLaufbahnplanung: () => routeSchuelerAbitur.data.managerLaufbahnplanung,
			ergebnisBelegpruefung: () => routeSchuelerAbitur.data.ergebnisBelegpruefung,
			managerAbitur: () => routeSchuelerAbitur.data.managerAbitur,
			copyAbiturdatenAusLeistungsdaten: routeSchuelerAbitur.data.copyAbiturdatenAusLeistungsdaten,
		};
	}

}

export const routeSchuelerAbiturZulassung = new RouteSchuelerAbiturZulassung();

