import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenAbiturFachProps } from "~/components/gost/fachwahlen/SGostFachwahlenAbiturFachProps";
import { ref } from "vue";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";


const SGostFachwahlenAbiturFach = () => import("~/components/gost/fachwahlen/SGostFachwahlenAbiturFach.vue");

export class RouteGostFachwahlenAbiturFach extends RouteNode<any, RouteGost> {

	private _idFach = ref<number>(-1);

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN
		], "gost.fachwahlen.abitur.fach", "abitur/fach/:idfach(\\d+)?", SGostFachwahlenAbiturFach);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Fachspezifisch";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? null : parseInt(params.abiturjahr);
		if ((abiturjahr === null) || (abiturjahr === -1))
			return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
		return false;
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if ((to_params.abiturjahr instanceof Array) || (to_params.idfach instanceof Array))
			return new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		// const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		this._idFach.value = !to_params.idfach ? -1 : parseInt(to_params.idfach);
		routeGostFachwahlen.data.auswahl = { idFach: this._idFach.value, bereich: 'Abitur' };
	}

	public getRoute(abiturjahr: number, idfach: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, idfach }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenAbiturFachProps {
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

export const routeGostFachwahlenAbiturFach = new RouteGostFachwahlenAbiturFach();
