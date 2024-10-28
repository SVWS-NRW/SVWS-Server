import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittAllgemeinProps } from "~/components/schueler/lernabschnitte/allgemein/SSchuelerLernabschnittAllgemeinProps";
import { api } from "~/router/Api";
import { routeSchueler } from "../RouteSchueler";

const SSchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/allgemein/SSchuelerLernabschnittAllgemein.vue");

export class RouteSchuelerLernabschnittAllgemein extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN ], "schueler.lernabschnitt.allgemein", "allgemein", SSchuelerLernabschnittAllgmein);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemein";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittAllgemeinProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerKompetenzenKlassen: api.benutzerKompetenzenKlassen,
			schule: api.schuleStammdaten,
			schuelerListeManager: () => routeSchueler.data.schuelerListeManager,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
		};
	}

}

export const routeSchuelerLernabschnittAllgemein = new RouteSchuelerLernabschnittAllgemein();

