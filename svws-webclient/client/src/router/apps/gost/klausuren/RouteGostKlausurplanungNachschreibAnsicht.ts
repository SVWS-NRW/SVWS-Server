import type { RouteParams } from "vue-router";

import type { DeveloperNotificationException } from "@core";
import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";

const SGostKlausurplanungNachschreibAnsicht = () => import("~/components/gost/klausuren/SGostKlausurplanungNachschreibAnsicht.vue");

export class RouteGostKlausurplanungNachschreibAnsicht extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
		], "gost.klausurplanung.nachschreibansicht", "nachschreibansicht", SGostKlausurplanungNachschreibAnsicht);
		super.mode = ServerMode.STABLE;
		super.text = "Nachschreibplan";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: undefined };
			if ((abiturjahr === undefined) || (abiturjahr === -1)) {
				return { name: routeGostKlausurplanung.defaultChild!.name, params };
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

}

export const routeGostKlausurplanungNachschreibAnsicht = new RouteGostKlausurplanungNachschreibAnsicht();
