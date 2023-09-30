import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import type { RouteSchuelerLeistungen } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungen";
import { routeSchuelerLeistungen } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungen";

import type { SchuelerLeistungenAuswahlProps } from "~/components/schueler/leistungsdaten/SSchuelerLeistungenAuswahlProps";
import type { SchuelerLeistungenDatenProps } from "~/components/schueler/leistungsdaten/SSchuelerLeistungenDatenProps";

const SSchuelerLeistungenDaten = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenDaten.vue");
const SSchuelerLeistungenAuswahl = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenAuswahl.vue")

export class RouteSchuelerLeistungenDaten extends RouteNode<unknown, RouteSchuelerLeistungen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.leistungen.daten", ":abschnitt(\\d+)?/:wechselNr(\\d+)?", SSchuelerLeistungenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.setView("lernabschnittauswahl", SSchuelerLeistungenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.abschnitt === undefined)
			return routeError.getRoute(new Error("Fehler: Kein Schuljahresabschnitt in der URL angegeben."));
		const idSchuljahresabschnitt = parseInt(to_params.abschnitt);
		if (to_params.wechselNr === undefined)
			return routeError.getRoute(new Error("Fehler: Keine Wechsel-Nummer in der URL angegeben."));
		const wechselNr = parseInt(to_params.wechselNr);
		await routeSchuelerLeistungen.data.setLernabschnitt(idSchuljahresabschnitt, wechselNr);
	}

	public getRoute(id: number, abschnitt: number | undefined, wechselNr: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerLeistungenAuswahlProps {
		return {
			lernabschnitt: routeSchuelerLeistungen.data.auswahl,
			lernabschnitte: routeSchuelerLeistungen.data.listAbschnitte,
			gotoLernabschnitt: routeSchuelerLeistungen.data.gotoLernabschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLeistungenDatenProps {
		return {
			manager: () => routeSchuelerLeistungen.data.manager,
			patchLeistung: routeSchuelerLeistungen.data.patchLeistung
		};
	}

}

export const routeSchuelerLeistungenDaten = new RouteSchuelerLeistungenDaten();

