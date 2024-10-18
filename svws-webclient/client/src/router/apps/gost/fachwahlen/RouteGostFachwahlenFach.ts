import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostFachwahlenFachProps } from "~/components/gost/fachwahlen/SGostFachwahlenFachProps";

import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import { ref } from "vue";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";


const SGostFachwahlenFach = () => import("~/components/gost/fachwahlen/SGostFachwahlenFach.vue");

export class RouteGostFachwahlenFach extends RouteNode<any, RouteGost> {

	private _idFach = ref<number>(-1);

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN
		], "gost.fachwahlen.fach", "fach/:idfach(\\d+)?", SGostFachwahlenFach);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Fachspezifisch";
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
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idfach: idFach } = RouteNode.getIntParams(to_params, ["idfach"]);
			this._idFach.value = idFach ?? -1;
			routeGostFachwahlen.data.auswahl = { idFach: this._idFach.value, bereich: 'Fach' };
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public getRoute(abiturjahr: number, idfach: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, idfach }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenFachProps {
		return {
			gotoLaufbahnplanung: routeGostFachwahlen.gotoLaufbahnplanung,
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
			fachwahlenManager: routeGostFachwahlen.data.fachwahlenManager,
			mapSchueler: routeGostFachwahlen.data.mapSchueler,
			faecherManager: routeGost.data.faecherManager,
			fachID: this._idFach.value,
		};
	}

}

export const routeGostFachwahlenFach = new RouteGostFachwahlenFach();
