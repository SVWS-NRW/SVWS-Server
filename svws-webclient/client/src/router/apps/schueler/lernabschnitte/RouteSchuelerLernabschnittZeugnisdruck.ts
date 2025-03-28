import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittZeugnisdruckProps } from "~/components/schueler/lernabschnitte/zeugnisdruck/SSchuelerLernabschnittZeugnisdruckProps";
import { api } from "~/router/Api";

const SSchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/zeugnisdruck/SSchuelerLernabschnittZeugnisdruck.vue");

export class RouteSchuelerLernabschnittZeugnisdruck extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN ], "schueler.lernabschnitt.zeugnisdruck", "zeugnisdruck", SSchuelerLernabschnittAllgmein);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeugnisdruck";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittZeugnisdruckProps {
		return {
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchBemerkungen: routeSchuelerLernabschnitte.data.patchBemerkungen
		};
	}

}

export const routeSchuelerLernabschnittZeugnisdruck = new RouteSchuelerLernabschnittZeugnisdruck();

