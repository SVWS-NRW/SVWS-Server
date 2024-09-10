import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostHalbjahr, ServerMode, DeveloperNotificationException } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";

import type { GostKlausurplanungSchienenProps } from "~/components/gost/klausurplanung/SGostKlausurplanungSchienenProps";
import { routeError } from "~/router/error/RouteError";
import { routeApp } from "../../RouteApp";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausurplanung/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung.schienen", "schienen", SGostKlausurplanungSchienen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schienen";
	}
	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe nochmals Abiturjahrgang, Halbjahr und ID der Blockung
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Die Parameter dürfen keine Arrays sein"));
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			return routeError.getRoute(new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein."));
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungSchienenProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => routeGostKlausurplanung.data.manager,
			patchKlausur: routeGostKlausurplanung.data.patchKlausur,
			createSchuelerklausurTermin: routeGostKlausurplanung.data.createSchuelerklausurTermin,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			erzeugeKlausurtermin: routeGostKlausurplanung.data.erzeugeKlausurtermin,
			loescheKlausurtermine: routeGostKlausurplanung.data.loescheKlausurtermine,
			erzeugeKursklausurenAusVorgaben: routeGostKlausurplanung.data.erzeugeKursklausurenAusVorgaben,
			blockenKursklausuren: routeGostKlausurplanung.data.blockenKursklausuren,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			gotoVorgaben: routeGostKlausurplanung.data.gotoVorgaben,
		}
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();


