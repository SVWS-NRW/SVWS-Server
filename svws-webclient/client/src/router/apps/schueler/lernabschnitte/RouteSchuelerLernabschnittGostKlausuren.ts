import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, JahrgangsUtils, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittGostKlausurenProps } from "~/components/schueler/lernabschnitte/gostKlausuren/SSchuelerLernabschnittGostKlausurenProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SSchuelerLernabschnittGostKlausuren = () => import("~/components/schueler/lernabschnitte/gostKlausuren/SSchuelerLernabschnittGostKlausuren.vue");

export class RouteSchuelerLernabschnittGostKlausuren extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION ], "schueler.lernabschnitt.gostklausuren", "gostklausuren", SSchuelerLernabschnittGostKlausuren);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klausuren";
		super.children = [
		];
		this.isHidden = (params?: RouteParams) => {
			if ((params === undefined) || (params.id === undefined) || (params.id instanceof Array))
				return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter der Route sind nicht gültig gesetzt."));
			const manager = routeSchueler.data.schuelerListeManager;
			if (!manager.hasDaten())
				return false;
			const abiturjahr = manager.auswahl().abiturjahrgang;
			const jahrgang = manager.jahrgaenge.get(manager.auswahl().idJahrgang);
			if (((abiturjahr !== null) && (manager.abiturjahrgaenge.get(abiturjahr) !== null))
				&& ((jahrgang !== null) && (JahrgangsUtils.istGymOb(jahrgang.kuerzelStatistik))))
				return false;
			return routeSchueler.getRoute(parseInt(params.id));
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number, abschnitt: number, wechselNr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittGostKlausurenProps {
		return {
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			kMan: () => routeSchuelerLernabschnitte.data.klausurManager,
			hatKlausurManager: () => routeSchuelerLernabschnitte.data.hatKlausurManager,
			createSchuelerklausurTermin: routeSchuelerLernabschnitte.data.createSchuelerklausurTermin,
			deleteSchuelerklausurTermin: routeSchuelerLernabschnitte.data.deleteSchuelerklausurTermin,
			patchSchuelerklausurTermin: routeSchuelerLernabschnitte.data.patchSchuelerklausurTermin
		};
	}

}

export const routeSchuelerLernabschnittGostKlausuren = new RouteSchuelerLernabschnittGostKlausuren();

