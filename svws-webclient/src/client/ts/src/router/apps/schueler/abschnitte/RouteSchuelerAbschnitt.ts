import type { RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";

import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerAbschnittDaten } from "~/router/apps/schueler/abschnitte/RouteSchuelerAbschnittDaten";
import { RouteDataSchuelerAbschnitt } from "~/router/apps/schueler/abschnitte/RouteDataSchuelerAbschnitt";


const SSchuelerAbschnitt = () => import("~/components/schueler/abschnitt/SSchuelerAbschnitt.vue");

export class RouteSchuelerAbschnitt extends RouteNode<RouteDataSchuelerAbschnitt, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.abschnitt", "abschnitt", SSchuelerAbschnitt, new RouteDataSchuelerAbschnitt());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => routeSchueler.getProps(route);
		super.text = "Lernabschnitte";
		super.children = [
			routeSchuelerAbschnittDaten
		];
		super.defaultChild = routeSchuelerAbschnittDaten;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.id === undefined)
			return routeError.getRoute(new Error("Fehler: Keine Schüler-ID in der URL angegeben."));
		const id = parseInt(to_params.id);
		if ((this.data.idSchueler !== id) || (to_params.abschnitt === undefined)) {
			await this.data.ladeListe(id);
			if (this.data.listAbschnitte.isEmpty())
				return routeError.getRoute(new Error("Fehler: Kein Lernabschnitt vorhanden"));
			if (to_params.abschnitt !== undefined) {
				const abschnitt = parseInt(to_params.abschnitt);
				const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? 0 : parseInt(to_params.wechselNr);
				const lernabschnitt = this.data.getEntry(abschnitt, wechselNr);
				if (lernabschnitt !== undefined)
					return routeSchuelerAbschnittDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === 0 ? undefined : lernabschnitt.wechselNr);
				if (wechselNr !== 0) {
					const lernabschnitt = this.data.getEntry(abschnitt, null);
					if (lernabschnitt !== undefined)
						return routeSchuelerAbschnittDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === 0 ? undefined : lernabschnitt.wechselNr);
				}
			}
			const lernabschnitt = this.data.getEntryDefault();
			if (lernabschnitt === undefined)
				return routeError.getRoute(new Error("Fehler: Kein Lernabschnitt gefunden"));
			return routeSchuelerAbschnittDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === 0 ? undefined : lernabschnitt.wechselNr);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

}

export const routeSchuelerAbschnitt = new RouteSchuelerAbschnitt();

