import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKursklausurManager, Schulform, ArrayList, ServerMode, GostKlausurvorgabenManager, DeveloperNotificationException, GostHalbjahr } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungKalenderProps } from "~/components/gost/klausurplanung/SGostKlausurplanungKalenderProps";
import { routeApp } from "../../RouteApp";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.kalender", "kalender/:kw(\\d+)?/:idTermin(\\d+)?", SGostKlausurplanungKalender);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalender";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (!routeGostKlausurplanung.data.hatStundenplanManager)
			return { name: routeGostKlausurplanung.defaultChild!.name, params };
		return false;
	}

	public getRoute(abiturjahr: number, halbjahr: number, kw: number | undefined, idTermin: number | undefined ) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr, kw, idTermin }};
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe die Parameter zunächst allgemein
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.kw instanceof Array || to_params.idTermin instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = !to_params.halbjahr ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		const termin = !to_params.idTermin ? undefined : routeGostKlausurplanung.data.kursklausurmanager.terminGetByIdOrNull(parseInt(to_params.idTermin)) || undefined;
		routeGostKlausurplanung.data.terminSelected.value = termin;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
		const kwEntry = (!to_params.kw || to_params.kw.length < 6 ) ? undefined : to_params.kw;
		const kwJahr = kwEntry !== undefined ? parseInt(kwEntry.substring(0, 4)) : -1;
		const kwWeek = kwEntry !== undefined ? parseInt(kwEntry.substring(4)) : -1;
		const kwAlt = (from !== undefined) && (from.name === to.name) && (!(from_params.kw instanceof Array)) && (from_params.kw !== undefined) ? parseInt(from_params.kw) : undefined;
		if ((kwEntry === undefined) && (kwAlt === undefined)) {
			const kwNeu = routeGostKlausurplanung.data.kalenderwoche.value.jahr === -1 ? routeGostKlausurplanung.data.stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString()) : routeGostKlausurplanung.data.kalenderwoche.value;
			return this.getRoute(abiturjahr, halbjahr.id, parseInt(kwNeu.jahr + "" + kwNeu.kw), termin ? termin.id : termin);
		} else if ((kwEntry === undefined) && (kwAlt !== undefined)) {
			return this.getRoute(abiturjahr, halbjahr.id, kwAlt, termin ? termin.id : termin);
		} else if (kwEntry !== undefined) {
			routeGostKlausurplanung.data.kalenderwoche.value = routeGostKlausurplanung.data.stundenplanmanager.kalenderwochenzuordnungGetByJahrAndKWOrClosest(kwJahr, kwWeek);
		}
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungKalenderProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager()},
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			stundenplanmanager: () => routeGostKlausurplanung.data.stundenplanmanager,
			hatStundenplanManager: routeGostKlausurplanung.data.hatStundenplanManager,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			zeigeAlleJahrgaenge: () => routeGostKlausurplanung.data.zeigeAlleJahrgaenge,
			setZeigeAlleJahrgaenge: routeGostKlausurplanung.data.setZeigeAlleJahrgaenge,
			kalenderwoche: routeGostKlausurplanung.data.kalenderwoche,
			terminSelected: routeGostKlausurplanung.data.terminSelected,
			gotoKalenderwoche: routeGostKlausurplanung.data.gotoKalenderwoche,
		}
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

