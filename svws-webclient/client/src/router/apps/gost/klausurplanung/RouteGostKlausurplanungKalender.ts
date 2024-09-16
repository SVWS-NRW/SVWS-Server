import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, ServerMode, DeveloperNotificationException, GostHalbjahr } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungKalenderProps } from "~/components/gost/klausurplanung/SGostKlausurplanungKalenderProps";
import { routeApp } from "../../RouteApp";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung.kalender", "kalender/:kw(\\d+)?/:idtermin(\\d+)?", SGostKlausurplanungKalender);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalender";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (!routeGostKlausurplanung.data.manager.getStundenplanManagerOrNull())
			return { name: routeGostKlausurplanung.defaultChild!.name, params };
		return false;
	}

	public getRoute(abiturjahr: number, halbjahr: number, kw: number | undefined, idtermin: number | undefined ) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr, kw, idtermin }};
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			const { kw } = RouteNode.getStringParams(to_params, ["kw"]);
			const { kw: kwFrom } = RouteNode.getStringParams(from_params, ["kw"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			const termin = routeGostKlausurplanung.data.manager.terminGetByIdOrNull(idtermin ?? -1) ?? undefined;
			routeGostKlausurplanung.data.terminSelected.value = termin ?? undefined;
			if ((abiturjahr === undefined) || (halbjahr === null))
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
			const kwEntry = (kw && kw.length < 6 ) ? undefined : kw;
			const kwJahr = (kwEntry !== undefined) ? parseInt(kwEntry.substring(0, 4)) : -1;
			const kwWeek = (kwEntry !== undefined) ? parseInt(kwEntry.substring(4)) : -1;
			if ((kwEntry === undefined) && (kwFrom === undefined)) {
				const kwNeu = routeGostKlausurplanung.data.kalenderwoche.value.jahr === -1
					? routeGostKlausurplanung.data.manager.getStundenplanManager().kalenderwochenzuordnungGetByDatum(new Date().toISOString())
					: routeGostKlausurplanung.data.kalenderwoche.value;
				return this.getRoute(abiturjahr, halbjahr.id, parseInt(kwNeu.jahr.toString() + "" + kwNeu.kw.toString()), termin ? termin.id : termin);
			} else if ((kwEntry === undefined) && (kwFrom !== undefined)) {
				return this.getRoute(abiturjahr, halbjahr.id, kwFrom, termin ? termin.id : termin);
			} else if (kwEntry !== undefined) {
				routeGostKlausurplanung.data.kalenderwoche.value = routeGostKlausurplanung.data.manager.getStundenplanManager().kalenderwochenzuordnungGetByJahrAndKWOrClosest(kwJahr, kwWeek);
			}
		} catch(e) {
			return routeError.getRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungKalenderProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => routeGostKlausurplanung.data.manager,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
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

