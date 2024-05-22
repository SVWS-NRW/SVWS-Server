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

