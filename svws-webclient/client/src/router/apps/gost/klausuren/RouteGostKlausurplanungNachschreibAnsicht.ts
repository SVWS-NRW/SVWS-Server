import type { RouteParams } from "vue-router";

import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { type RouteGostKlausurplanung, routeGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { routeError } from "~/router/error/RouteError";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";

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
