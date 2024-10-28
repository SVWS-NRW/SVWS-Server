import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerAbschluesse } from "~/router/apps/schueler/abschluesse/RouteDataSchuelerAbschluesse";
import { type SchuelerAbschluesseProps } from "~/components/schueler/abschluesse/SchuelerAbschluesseProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SSchuelerAbschluesse = () => import("~/components/schueler/abschluesse/SSchuelerAbschluesse.vue");

export class RouteSchuelerAbschluesse extends RouteNode<RouteDataSchuelerAbschluesse, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f => !f.equals(Schulform.G)), [ BenutzerKompetenz.KEINE ], "schueler.abschluesse", "abschluesse", SSchuelerAbschluesse, new RouteDataSchuelerAbschluesse());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abschlüsse";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			if (id === undefined) {
				await this.data.auswahlSchueler(null);
				return;
			}
			try {
				await this.data.auswahlSchueler(routeSchueler.data.schuelerListeManager.liste.get(id));
			} catch(error) {
				return routeSchueler.getRoute(id);
			}
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.clear();
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbschluesseProps {
		return {
			schuelerListeManager: () => routeSchueler.data.schuelerListeManager,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			serverMode: api.mode,
		};
	}

}

export const routeSchuelerAbschluesse = new RouteSchuelerAbschluesse();

