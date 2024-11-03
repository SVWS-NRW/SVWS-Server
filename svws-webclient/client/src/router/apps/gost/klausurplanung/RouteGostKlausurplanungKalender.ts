import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, ServerMode, DeveloperNotificationException, GostHalbjahr, DateUtils } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungKalenderProps } from "~/components/gost/klausurplanung/SGostKlausurplanungKalenderProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";
import { routeGostKlausurplanungVorgaben } from "./RouteGostKlausurplanungVorgaben";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung.kalender", "kalender/:datum(\\d+)?/:idtermin(\\d+)?", SGostKlausurplanungKalender);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalender";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (!routeGostKlausurplanung.data.abschnitt || routeGostKlausurplanung.data.manager.stundenplanManagerGeladenAndExistsByAbschnitt(routeGostKlausurplanung.data.abschnitt.id) === false)
			return { name: routeGostKlausurplanung.defaultChild!.name, params };
		return false;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (!routeGostKlausurplanung.data.manager.stundenplanManagerExistsByAbschnitt(routeGostKlausurplanung.data.abschnitt!.id))
				return routeGostKlausurplanungVorgaben.getRoute();
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			let { datum } = RouteNode.getStringParams(to_params, ["datum"]);
			if (datum !== undefined)
				datum = datum.slice(0, 4) + "-" + datum.slice(4, 6) + "-" + datum.slice(6, 8);
			let { datum: datumFrom } = RouteNode.getStringParams(from_params, ["datum"]);
			if (datumFrom !== undefined)
				datumFrom = datumFrom.slice(0, 4) + "-" + datumFrom.slice(4, 6) + "-" + datumFrom.slice(6, 8);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			const termin = routeGostKlausurplanung.data.manager.terminGetByIdOrNull(idtermin ?? -1) ?? undefined;
			routeGostKlausurplanung.data.terminSelected.value = termin ?? undefined;
			if ((abiturjahr === undefined) || (halbjahr === null))
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
			if ((datum === undefined) && (datumFrom === undefined)) {
				let datumNeu = routeGostKlausurplanung.data.kalenderdatum.value === undefined ? new Date().toISOString().slice(0, 10) : routeGostKlausurplanung.data.kalenderdatum.value;
				const stundenplan = routeGostKlausurplanung.data.manager.stundenplanManagerGetByAbschnittAndDatumOrClosest(routeGostKlausurplanung.data.abschnitt!.id, datumNeu);
				const kwClosest = stundenplan.kalenderwochenzuordnungGetByDatum(datumNeu);
				datumNeu = DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kwClosest.jahr, kwClosest.kw);
				return this.getRoute({ datum: datumNeu.replace(/-/g, ""), idtermin: termin === undefined ? undefined : termin.id });
			} else if ((datum === undefined) && (datumFrom !== undefined)) {
				return this.getRoute({ datum: datumFrom.replace(/-/g, ""), idtermin: termin === undefined ? undefined : termin.id });
			} else if (datum !== undefined) {
				const stundenplan = routeGostKlausurplanung.data.manager.stundenplanManagerGetByAbschnittAndDatumOrClosest(routeGostKlausurplanung.data.abschnitt!.id, datum);
				routeGostKlausurplanung.data.kalenderdatum.value = datum;
			}
		} catch(e) {
			return routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		const datum = routeGostKlausurplanung.data.kalenderdatum.value?.replace(/-/g, "") ?? undefined;
		const idtermin = routeGostKlausurplanung.data.terminSelected.value?.id ?? undefined;
		return { datum, idtermin };
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungKalenderProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			abschnitt: routeGostKlausurplanung.data.abschnitt,
			kMan: () => routeGostKlausurplanung.data.manager,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			zeigeAlleJahrgaenge: () => routeGostKlausurplanung.data.zeigeAlleJahrgaenge,
			setZeigeAlleJahrgaenge: routeGostKlausurplanung.data.setZeigeAlleJahrgaenge,
			kalenderdatum: routeGostKlausurplanung.data.kalenderdatum,
			terminSelected: routeGostKlausurplanung.data.terminSelected,
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
		}
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

