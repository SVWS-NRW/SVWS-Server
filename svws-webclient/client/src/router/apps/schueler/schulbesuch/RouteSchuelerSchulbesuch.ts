import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteDataSchuelerSchulbesuch";

import type { SchuelerSchulbesuchProps } from "~/components/schueler/schulbesuch/SSchuelerSchulbesuchProps";
import { routeApp } from "../../RouteApp";
import { api } from "~/router/Api";

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN ], "schueler.schulbesuch", "schulbesuch", SSchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.id !== undefined) {
			const id = parseInt(to_params.id);
			await this.data.setEintrag(id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerSchulbesuchProps {
		return {
			patch: this.data.patch,
			schuelerListeManager: () => routeSchueler.data.schuelerListeManager,
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			data: this.data.daten,
			autofocus: routeSchueler.data.autofocus
		};
	}

}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();

