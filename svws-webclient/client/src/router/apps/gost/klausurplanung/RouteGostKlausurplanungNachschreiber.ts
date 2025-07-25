import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostHalbjahr, ServerMode, DeveloperNotificationException } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";

import type { GostKlausurplanungNachschreiberProps } from "~/components/gost/klausurplanung/SGostKlausurplanungNachschreiberProps";
import { routeError } from "~/router/error/RouteError";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostKlausurplanungNachschreiber = () => import("~/components/gost/klausurplanung/SGostKlausurplanungNachschreiber.vue");

export class RouteGostKlausurplanungNachschreiber extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung.nachschreiber", "nachschreiber", SGostKlausurplanungNachschreiber);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nachschreiber";
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: undefined };
			return ((abiturjahr === undefined) || (abiturjahr === -1))
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			if ((abiturjahr === undefined) || (halbjahr === null))
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
		} catch(e) {
			return await routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungNachschreiberProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => routeGostKlausurplanung.data.manager,
			patchKlausur: routeGostKlausurplanung.data.patchKlausur,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			erzeugeKlausurtermin: routeGostKlausurplanung.data.erzeugeKlausurtermin,
			loescheKlausurtermine: routeGostKlausurplanung.data.loescheKlausurtermine,
			blockenNachschreibklausuren: routeGostKlausurplanung.data.blockenNachschreiber,
			updateKlausurblockung: routeGostKlausurplanung.data.updateKlausurblockung,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			zeigeAlleJahrgaenge: () => routeGostKlausurplanung.data.zeigeAlleJahrgaenge,
			setZeigeAlleJahrgaenge: routeGostKlausurplanung.data.setZeigeAlleJahrgaenge,
			gotoNachschreiber: routeGostKlausurplanung.data.gotoNachschreiber,
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
		}
	}

}

export const routeGostKlausurplanungNachschreiber = new RouteGostKlausurplanungNachschreiber();


