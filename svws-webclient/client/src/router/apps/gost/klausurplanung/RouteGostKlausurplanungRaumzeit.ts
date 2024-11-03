import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungRaumzeitProps } from "~/components/gost/klausurplanung/SGostKlausurplanungRaumzeitProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";

const SGostKlausurplanungRaumzeit = () => import("~/components/gost/klausurplanung/SGostKlausurplanungRaumzeit.vue");

export class RouteGostKlausurplanungRaumzeit extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung.raumzeit", "raumzeit/:idtermin(\\d+)?", SGostKlausurplanungRaumzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume und Startzeiten";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (!routeGostKlausurplanung.data.abschnitt || routeGostKlausurplanung.data.manager.stundenplanManagerGeladenAndExistsByAbschnitt(routeGostKlausurplanung.data.abschnitt.id) === false)
			return { name: routeGostKlausurplanung.defaultChild!.name, params };
		return false;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			if ((abiturjahr === undefined) || (halbjahr === null))
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
			const terminList = routeGostKlausurplanung.data.manager.terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(routeGostKlausurplanung.data.jahrgangsdaten.abiturjahr, routeGostKlausurplanung.data.halbjahr, routeGostKlausurplanung.data.quartalsauswahl.value);
			if ((idtermin === undefined) && !terminList.isEmpty()) {
				const termin = (routeGostKlausurplanung.data.terminSelected.value !== undefined) && terminList.contains(routeGostKlausurplanung.data.terminSelected.value) ? routeGostKlausurplanung.data.terminSelected.value : terminList.getFirst();
				return this.getRoute({ idtermin: termin.id });
			}
			if (idtermin !== undefined) {
				const termin = routeGostKlausurplanung.data.manager.terminGetByIdOrException(idtermin);
				// routeGostKlausurplanung.data.terminSelected.value = termin;
				routeGostKlausurplanung.data.setRaumTermin(termin);
			}
		} catch (e) {
			return routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungRaumzeitProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			abschnitt: routeGostKlausurplanung.data.abschnitt,
			gotoTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
			kMan: () => routeGostKlausurplanung.data.manager,
			createKlausurraum: routeGostKlausurplanung.data.createKlausurraum,
			loescheKlausurraum: routeGostKlausurplanung.data.loescheKlausurraum,
			patchKlausurraum: routeGostKlausurplanung.data.patchKlausurraum,
			setzeRaumZuSchuelerklausuren: routeGostKlausurplanung.data.setzeRaumZuSchuelerklausuren,
			patchKlausur: routeGostKlausurplanung.data.patchKlausur,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			setRaumTermin: routeGostKlausurplanung.data.setRaumTermin,
			terminSelected: routeGostKlausurplanung.data.terminSelected,
			zeigeAlleJahrgaenge: () => routeGostKlausurplanung.data.zeigeAlleJahrgaenge,
			setZeigeAlleJahrgaenge: routeGostKlausurplanung.data.setZeigeAlleJahrgaenge,
			setConfigValue: routeGostKlausurplanung.data.setConfigValue,
			getConfigValue: routeGostKlausurplanung.data.getConfigValue,
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
		}
	}

}

export const routeGostKlausurplanungRaumzeit = new RouteGostKlausurplanungRaumzeit();

