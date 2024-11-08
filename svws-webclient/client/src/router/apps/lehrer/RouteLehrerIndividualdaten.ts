import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";

import type { LehrerIndividualdatenProps } from "~/components/lehrer/individualdaten/SLehrerIndividualdatenProps";
import { api } from "~/router/Api";

const SLehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/SLehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_ANSEHEN ], "lehrer.daten", "daten", SLehrerIndividualdaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	public getProps(to: RouteLocationNormalized): LehrerIndividualdatenProps {
		return {
			validatorKontext: () => api.validatorKontext,
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeLehrer.data.patch,
			lehrerListeManager: () => routeLehrer.data.manager,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile
		};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();

