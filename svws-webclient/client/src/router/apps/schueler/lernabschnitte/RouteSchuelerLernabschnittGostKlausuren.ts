import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittGostKlausurenProps } from "~/components/schueler/lernabschnitte/gostklausuren/SchuelerLernabschnittGostKlausurenProps";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeSchuelerLernabschnittAllgemein } from "./RouteSchuelerLernabschnittAllgemein";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuelerLernabschnittGostKlausuren = () => import("~/components/schueler/lernabschnitte/gostklausuren/SchuelerLernabschnittGostKlausuren.vue");

export class RouteSchuelerLernabschnittGostKlausuren extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
		], "schueler.lernabschnitt.gostklausuren", "gostklausuren", SchuelerLernabschnittGostKlausuren);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klausuren";
		super.children = [
		];
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
	}

	protected checkHidden(to_params?: RouteParams) {
		try {
			const { id, abschnitt, wechselNr } = (to_params === undefined) ? { id: undefined, abschnitt: undefined, wechselNr: undefined } : RouteNode.getIntParams(to_params, ["id", "abschnitt", "wechselNr"]);
			if ((id === undefined) || (abschnitt === undefined) || (wechselNr === undefined)) {
				throw new DeveloperNotificationException("Fehler: Die Parameter der Route sind nicht gültig gesetzt.");
			}
			if (routeSchueler.data.manager.hasDaten()) {
				const abiturjahr = routeSchueler.data.manager.auswahl().abiturjahrgang;
				if (((abiturjahr !== null) && routeSchueler.data.manager.abiturjahrgaenge.get(abiturjahr))
				&& (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN)
					|| (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION) && benutzerStateImpl.kompetenzenAbiturjahrgaenge.has(abiturjahr)))) {
					if (routeSchuelerLernabschnitte.data.hatGymOb) {
						return false;
					}
				}
			}
			return routeSchuelerLernabschnittAllgemein.getRoute({ id, abschnitt, wechselNr });
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittGostKlausurenProps {
		return {
			manager: () => routeSchuelerLernabschnitte.data.manager,
			kMan: () => routeSchuelerLernabschnitte.data.klausurManager,
			hatKlausurManager: () => routeSchuelerLernabschnitte.data.hatKlausurManager,
			createSchuelerklausurtermin: routeSchuelerLernabschnitte.data.createSchuelerklausurtermin,
			deleteSchuelerklausurtermin: routeSchuelerLernabschnitte.data.deleteSchuelerklausurtermin,
			patchSchuelerklausurtermin: routeSchuelerLernabschnitte.data.patchSchuelerklausurtermin,
			patchSchuelerklausur: routeSchuelerLernabschnitte.data.patchSchuelerklausur,
			gotoPlanung: routeSchuelerLernabschnitte.data.gotoPlanung,
		};
	}

}

export const routeSchuelerLernabschnittGostKlausuren = new RouteSchuelerLernabschnittGostKlausuren();
