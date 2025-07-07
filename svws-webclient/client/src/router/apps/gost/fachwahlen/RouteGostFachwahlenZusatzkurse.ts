import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenZusatzkurseProps } from "~/components/gost/fachwahlen/SGostFachwahlenZusatzkurseProps";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";


const SGostFachwahlenZusatzkurse = () => import("~/components/gost/fachwahlen/SGostFachwahlenZusatzkurse.vue");

export class RouteGostFachwahlenZusatzkurse extends RouteNode<any, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "gost.fachwahlen.zusatzkurse", "zusatzkurse", SGostFachwahlenZusatzkurse);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Zusatzkurse";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: null };
			if ((abiturjahr === null) || (abiturjahr === -1))
				return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// if (to_params.abiturjahr instanceof Array)
		// 	return new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		// const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		routeGostFachwahlen.data.auswahl = { bereich: 'ZK' };
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenZusatzkurseProps {
		return {
			gotoLaufbahnplanung: routeGostFachwahlen.gotoLaufbahnplanung,
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
			fachwahlenManager: routeGostFachwahlen.data.fachwahlenManager,
			mapSchueler: routeGostFachwahlen.data.mapSchueler,
			faecherManager: routeGost.data.faecherManager,
		};
	}

}

export const routeGostFachwahlenZusatzkurse = new RouteGostFachwahlenZusatzkurse();
