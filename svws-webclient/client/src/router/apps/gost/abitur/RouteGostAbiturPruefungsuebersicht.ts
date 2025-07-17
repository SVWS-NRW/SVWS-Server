import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostAbitur, type RouteGostAbitur } from "~/router/apps/gost/abitur/RouteGostAbitur";

import type { GostAbiturPruefungsuebersichtProps } from "~/components/gost/abitur/GostAbiturPruefungsuebersichtProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeGost } from "../RouteGost";
import { routeError } from "~/router/error/RouteError";

const GostAbiturPruefungsuebersicht = () => import("~/components/gost/abitur/GostAbiturPruefungsuebersicht.vue");

export class RouteGostAbiturPruefungsuebersicht extends RouteNode<any, RouteGostAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "gost.abitur.pruefungsuebersicht", "pruefungsuebersicht", GostAbiturPruefungsuebersicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Prüfung";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		super.children = [
		];
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = (params !== undefined) ? RouteNode.getIntParams(params, ["abiturjahr"]) : {abiturjahr: undefined};
			if (abiturjahr === undefined)
				return false;
			const eintrag = routeGost.data.mapAbiturjahrgaenge.get(abiturjahr);
			if (eintrag === undefined)
				return false;
			if ((eintrag.abiturjahr !== -1)
				&& (api.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN)
					|| (api.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN) && api.benutzerKompetenzenAbiturjahrgaenge.has(eintrag.abiturjahr)))
				&& (eintrag.jahrgang === 'Q2') && (eintrag.halbjahr === 2))
				return false;
			return routeGost.getRouteDefaultChild({ abiturjahr });
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): GostAbiturPruefungsuebersichtProps {
		return {
			serverMode: api.mode,
			schule: api.schuleStammdaten,
			schuelerListe: routeGostAbitur.data.schuelerListe,
			managerMap: () => routeGostAbitur.data.managerAbiturMap,
			updateAbiturpruefungsdaten: routeGostAbitur.data.updateAbiturpruefungsdaten,
		};
	}

}

export const routeGostAbiturPruefungsuebersicht = new RouteGostAbiturPruefungsuebersicht();

