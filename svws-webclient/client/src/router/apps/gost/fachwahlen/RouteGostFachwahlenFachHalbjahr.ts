import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenFachHalbjahrProps } from "~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahrProps";
import { ref } from "vue";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";


const SGostFachwahlenFachHalbjahr = () => import("~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahr.vue");

export class RouteGostFachwahlenFachHalbjahr extends RouteNode<any, RouteGost> {

	private _idFach = ref<number>(-1);
	private _halbjahr = ref<GostHalbjahr>(GostHalbjahr.EF1);

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "gost.fachwahlen.fach.halbjahr", "fach/:idFach(\\d+)/halbjahr/:idHalbjahr(\\d+)", SGostFachwahlenFachHalbjahr);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Fach- und Halbjahresbezogen";
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
		try {
			const { idFach, idHalbjahr } = RouteNode.getIntParams(to_params, ["idFach", "idHalbjahr"]);
			this._idFach.value = idFach ?? -1;
			const halbjahr = GostHalbjahr.fromID(idHalbjahr ?? null);
			if (halbjahr === null)
				throw new DeveloperNotificationException("Fehler: Das Halbjahr " + idHalbjahr + " ist ungültig");
			this._halbjahr.value = halbjahr;
			routeGostFachwahlen.data.auswahl = { idFach: this._idFach.value, bereich: halbjahr.kuerzel };
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		const idFach = this._idFach.value;
		const idHalbjahr = this._halbjahr.value.id;
		return { idFach, idHalbjahr };
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
