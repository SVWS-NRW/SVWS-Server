import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";

import { api } from "~/router/Api";
import { RouteDataSchuelerLernplattformen } from "~/router/apps/schueler/lernplattformen/RouteDataSchuelerLernplattformen";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { SchuelerLernplattformenProps } from "~/components/schueler/lernplattformen/SchuelerLernplattformenProps";

const SSchuelerLernplattformen = () => import("~/components/schueler/lernplattformen/SSchuelerLernplattformen.vue");

export class RouteSchuelerLernplattformen extends RouteNode<RouteDataSchuelerLernplattformen, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN ], "schueler.lernplattformen", "lernplattformen", SSchuelerLernplattformen, new RouteDataSchuelerLernplattformen());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattformen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined)
				await this.data.ladeDaten(null);
			else
				await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernplattformenProps {
		return {
			schuelerLernplattformen: () => this.data.schuelerLernplattformen,
			mapLernplattformen: this.data.mapLernplattformen,
			patch: this.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
			apiStatus: api.status,
		};
	}
}

export const routeSchuelerLernplattformen = new RouteSchuelerLernplattformen();

