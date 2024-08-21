import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittLeistungenProps } from "~/components/schueler/lernabschnitte/leistungen/SSchuelerLernabschnittLeistungenProps";
import { routeApp } from "../../RouteApp";
import { routeSchueler } from "../RouteSchueler";

const SSchuelerLernabschnittLeistungen = () => import("~/components/schueler/lernabschnitte/leistungen/SSchuelerLernabschnittLeistungen.vue");

export class RouteSchuelerLernabschnittLeistungen extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN ], "schueler.lernabschnitt.leistungen", "leistungen", SSchuelerLernabschnittLeistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number, abschnitt: number, wechselNr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittLeistungenProps {
		return {
			serverMode: api.mode,
			benutzerdaten: api.benutzerdaten,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerKompetenzenKlassen: api.benutzerKompetenzenKlassen,
			schule: api.schuleStammdaten,
			schulform: api.schulform,
			schuelerListeManager: () => routeSchueler.data.schuelerListeManager,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchLeistung: routeSchuelerLernabschnitte.data.patchLeistung,
			addLeistung: routeSchuelerLernabschnitte.data.addLeistung,
			deleteLeistungen: routeSchuelerLernabschnitte.data.deleteLeistungen,
		};
	}

}

export const routeSchuelerLernabschnittLeistungen = new RouteSchuelerLernabschnittLeistungen();

