import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenHalbjahrProps } from "~/components/gost/fachwahlen/SGostFachwahlenHalbjahrProps";
import { ref } from "vue";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";


const SGostFachwahlenHalbjahr = () => import("~/components/gost/fachwahlen/SGostFachwahlenHalbjahr.vue");

export class RouteGostFachwahlenHalbjahr extends RouteNode<any, RouteGost> {

	private _halbjahr = ref<GostHalbjahr>(GostHalbjahr.EF1);

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "gost.fachwahlen.halbjahr", "halbjahr/:idHalbjahr(\\d+)?", SGostFachwahlenHalbjahr);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Halbjahresbezogen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, [ "abiturjahr" ]) : { abiturjahr: null };
			if ((abiturjahr === null) || (abiturjahr === -1))
				return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idHalbjahr } = RouteNode.getIntParams(to_params, [ "idHalbjahr" ]);
			const halbjahr = GostHalbjahr.fromID(idHalbjahr ?? null);
			if (halbjahr === null)
				throw new DeveloperNotificationException("Fehler: Das Halbjahr " + idHalbjahr + " ist ungültig");
			this._halbjahr.value = halbjahr;
			routeGostFachwahlen.data.auswahl = { bereich: halbjahr.kuerzel };
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		const idHalbjahr = this._halbjahr.value.id;
		return { idHalbjahr };
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenHalbjahrProps {
		return {
			gotoLaufbahnplanung: routeGostFachwahlen.gotoLaufbahnplanung,
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
			fachwahlenManager: routeGostFachwahlen.data.fachwahlenManager,
			mapSchueler: routeGostFachwahlen.data.mapSchueler,
			faecherManager: routeGost.data.faecherManager,
			halbjahr: this._halbjahr.value,
		};
	}

}

export const routeGostFachwahlenHalbjahr = new RouteGostFachwahlenHalbjahr();
