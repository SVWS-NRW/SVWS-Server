import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerSprachen } from "~/router/apps/schueler/sprachen/RouteDataSchuelerSprachen";
import { type SchuelerSprachenProps } from "~/components/schueler/sprachen/SchuelerSprachenProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SSchuelerSprachen = () => import("~/components/schueler/sprachen/SSchuelerSprachen.vue");

export class RouteSchuelerSprachen extends RouteNode<RouteDataSchuelerSprachen, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f => !f.equals(Schulform.G)), [ BenutzerKompetenz.KEINE ], "schueler.sprachen", "sprachen", SSchuelerSprachen, new RouteDataSchuelerSprachen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Sprachen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (this.parent === undefined)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert"));
		if (to_params.id === undefined) {
			await this.data.auswahlSchueler(null);
			return;
		}
		const id = parseInt(to_params.id);
		try {
			await this.data.auswahlSchueler(routeSchueler.data.schuelerListeManager.liste.get(id));
		} catch(error) {
			return routeSchueler.getRoute(id);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.clear();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerSprachenProps {
		return {
			sprachbelegungen: () => this.data.sprachbelegungen,
			sprachpruefungen: () => this.data.sprachpruefungen,
			patchSprachbelegung: this.data.patchSprachbelegung,
			addSprachbelegung: this.data.addSprachbelegung,
			removeSprachbelegung: this.data.removeSprachbelegung,
			patchSprachpruefung: this.data.patchSprachpruefung,
			addSprachpruefung: this.data.addSprachpruefung,
			removeSprachpruefung: this.data.removeSprachpruefung,
			schuelerListeManager: () => routeSchueler.data.schuelerListeManager,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerKompetenzenKlassen: api.benutzerKompetenzenKlassen,
		};
	}

}

export const routeSchuelerSprachen = new RouteSchuelerSprachen();

