import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerSprachen } from "~/router/apps/schueler/sprachen/RouteDataSchuelerSprachen";
import { type SchuelerSprachenProps } from "~/components/schueler/sprachen/SSchuelerSprachenProps";
import { api } from "~/router/Api";

const SSchuelerSprachen = () => import("~/components/schueler/sprachen/SSchuelerSprachen.vue");

export class RouteSchuelerSprachen extends RouteNode<RouteDataSchuelerSprachen, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f => !f.equals(Schulform.G)), [ BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN ], "schueler.sprachen", "sprachen", SSchuelerSprachen, new RouteDataSchuelerSprachen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Sprachen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			if (id === undefined)
				return await this.data.auswahlSchueler(null);
			try {
				await this.data.auswahlSchueler(routeSchueler.data.manager.liste.get(id));
			} catch(error) {
				return routeSchueler.getRouteDefaultChild({ id });
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.clear();
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
			schuelerListeManager: () => routeSchueler.data.manager,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerKompetenzenKlassen: api.benutzerKompetenzenKlassen,
		};
	}

}

export const routeSchuelerSprachen = new RouteSchuelerSprachen();

