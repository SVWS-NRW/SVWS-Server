import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenFachHalbjahrProps } from "~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahrProps";
import { ref } from "vue";
import { routeApp } from "../../RouteApp";


const SGostFachwahlenFachHalbjahr = () => import("~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahr.vue");

export class RouteGostFachwahlenFachHalbjahr extends RouteNode<any, RouteGost> {

	private _idFach = ref<number>(-1);
	private _halbjahr = ref<GostHalbjahr>(GostHalbjahr.EF1);

	public constructor() {
		super(Schulform.getMitGymOb(), [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN
		], "gost.fachwahlen.fach.halbjahr", "fach/:idfach(\\d+)/halbjahr/:idhalbjahr(\\d+)", SGostFachwahlenFachHalbjahr);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Fach- und Halbjahresbezogen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? null : parseInt(params.abiturjahr);
		if ((abiturjahr === null) || (abiturjahr === -1))
			return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr }};
		return false;
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if ((to_params.idhalbjahr instanceof Array) || (to_params.idfach instanceof Array))
			return new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		this._idFach.value = parseInt(to_params.idfach);
		const idHalbjahr = parseInt(to_params.idhalbjahr);
		const halbjahr = GostHalbjahr.fromID(idHalbjahr);
		if (halbjahr === null)
			return new DeveloperNotificationException("Fehler: Das Halbjahr " + to_params.idhalbjahr + " ist ungültig");
		this._halbjahr.value = halbjahr;
		routeGostFachwahlen.data.auswahl = { idFach: this._idFach.value, bereich: halbjahr.kuerzel };
	}

	public getRoute(abiturjahr: number, idfach: number, halbjahr: GostHalbjahr) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, idfach: idfach, idhalbjahr: halbjahr.id }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenFachHalbjahrProps {
		return {
			gotoLaufbahnplanung: routeGostFachwahlen.gotoLaufbahnplanung,
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
			fachwahlenManager: routeGostFachwahlen.data.fachwahlenManager,
			mapSchueler: routeGostFachwahlen.data.mapSchueler,
			faecherManager: routeGost.data.faecherManager,
			fachID: this._idFach.value,
			halbjahr: this._halbjahr.value,
		};
	}

}

export const routeGostFachwahlenFachHalbjahr = new RouteGostFachwahlenFachHalbjahr();
