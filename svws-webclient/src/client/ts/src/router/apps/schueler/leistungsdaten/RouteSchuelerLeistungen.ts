import type { RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";
import { RouteDataSchuelerLeistungen } from "~/router/apps/schueler/leistungsdaten/RouteDataSchuelerLeistungen";

const SSchuelerLeistungen = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungen.vue");


export class RouteSchuelerLeistungen extends RouteNode<RouteDataSchuelerLeistungen, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.leistungen", "leistungsdaten", SSchuelerLeistungen, new RouteDataSchuelerLeistungen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Leistungsdaten";
		super.children = [
			routeSchuelerLeistungenDaten
		];
		super.defaultChild = routeSchuelerLeistungenDaten;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id);
		if ((this.data.idSchueler !== id) || (to_params.abschnitt === undefined)) {
			await this.data.ladeListe(id);
			if (this.data.listAbschnitte.size()<=0)
				return false;
			if (to_params.abschnitt !== undefined) {
				const abschnitt = parseInt(to_params.abschnitt);
				const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? null : parseInt(to_params.wechselNr);
				const lernabschnitt = this.data.getEntry(abschnitt, wechselNr);
				if (lernabschnitt !== undefined)
					return routeSchuelerLeistungenDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === null ? undefined : lernabschnitt.wechselNr);
				if (wechselNr !== null) {
					const lernabschnitt = this.data.getEntry(abschnitt, null);
					if (lernabschnitt !== undefined)
						return routeSchuelerLeistungenDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === null ? undefined : lernabschnitt.wechselNr);
				}
			}
			const lernabschnitt = this.data.getEntryDefault();
			if (lernabschnitt === undefined)
				return false;
			return routeSchuelerLeistungenDaten.getRoute(id, lernabschnitt.schuljahresabschnitt, lernabschnitt.wechselNr === null ? undefined : lernabschnitt.wechselNr);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

}

export const routeSchuelerLeistungen = new RouteSchuelerLeistungen();

