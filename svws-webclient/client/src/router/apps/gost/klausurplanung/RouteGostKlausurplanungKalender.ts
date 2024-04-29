import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKursklausurManager, Schulform, ArrayList, ServerMode, GostKlausurvorgabenManager, DeveloperNotificationException, GostHalbjahr } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungKalenderProps } from "~/components/gost/klausurplanung/SGostKlausurplanungKalenderProps";
import { routeApp } from "../../RouteApp";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.kalender", "kalender/:kw(\\d+)?", SGostKlausurplanungKalender);
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

	public getRoute(abiturjahr: number, halbjahr: number, kw: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr, kw }};
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe die Parameter zunächst allgemein
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.kw instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = !to_params.halbjahr ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if ((abiturjahr === undefined) || (halbjahr === undefined))
			throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen definiert sein.");
		const kw = !to_params.kw ? undefined : parseInt(to_params.kw);
		const kwAlt = (from !== undefined) && (from.name === to.name) && (!(from_params.kw instanceof Array)) && (from_params.kw !== undefined) ? parseInt(from_params.kw) : undefined;
		if ((kw === undefined) && (kwAlt === undefined)) {
			const kwNeu = routeGostKlausurplanung.data.stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString()).kw;
			return this.getRoute(abiturjahr, halbjahr.id, kwNeu);
		} else if ((kw === undefined) && (kwAlt !== undefined)) {
			return this.getRoute(abiturjahr, halbjahr.id, kwAlt);
		} else if (kw !== undefined) {
			routeGostKlausurplanung.data.kalenderwoche.value = routeGostKlausurplanung.data.stundenplanmanager.kalenderwochenzuordnungGetByJahrAndKWOrException(2019, kw);
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
			gotoKalenderwoche: routeGostKlausurplanung.data.gotoKalenderwoche,
		}
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

