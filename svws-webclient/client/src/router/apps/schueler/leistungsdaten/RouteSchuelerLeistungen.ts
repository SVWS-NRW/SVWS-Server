import type { RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
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
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.id === undefined)
			return routeError.getRoute(new Error("Fehler: Keine Schüler-ID in der URL angegeben."));
		const id = parseInt(to_params.id);
		if (this.data.idSchueler !== id)
			await this.data.setSchueler(id);
		if ((to === this) && (this.data.hatAuswahl))
			return routeSchuelerLeistungenDaten.getRoute(id, this.data.auswahl.schuljahresabschnitt, this.data.auswahl.wechselNr);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

}

export const routeSchuelerLeistungen = new RouteSchuelerLeistungen();

