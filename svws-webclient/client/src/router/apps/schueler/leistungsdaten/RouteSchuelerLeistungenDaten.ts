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
		if (to_params.id instanceof Array || to_params.abschnitt instanceof Array || to_params.wechselNr instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.id === undefined)
			return routeError.getRoute(new Error("Fehler: Keine Schüler-ID in der URL angegeben."));
		const id = parseInt(to_params.id);
		if (to_params.abschnitt === undefined) {
			return routeSchuelerLeistungen.getRoute(id);
		} else {
			const abschnitt = parseInt(to_params.abschnitt);
			const wechselNr = (to_params.wechselNr === undefined) || (to_params.wechselNr === "") ? 0 : parseInt(to_params.wechselNr);
			const eintrag = routeSchuelerLeistungen.data.getEntry(abschnitt, wechselNr);
			await routeSchuelerLeistungen.data.setEintrag(eintrag);
		}
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
			data: routeSchuelerLeistungen.data.daten,
			mapFaecher: routeSchuelerLeistungen.data.mapFaecher,
			mapLehrer: routeSchuelerLeistungen.data.mapLehrer,
			mapKurse: routeSchuelerLeistungen.data.mapKurse,
			patchLeistung: routeSchuelerLeistungen.data.patchLeistung
		};
	}

}

export const routeSchuelerLeistungenDaten = new RouteSchuelerLeistungenDaten();

