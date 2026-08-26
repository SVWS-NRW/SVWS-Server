import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { checkHiddenKlausurplanungStundenplan, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import SGostKlausurplanungDetailAnsichtVue from "~/components/gost/klausuren/SGostKlausurplanungDetailAnsicht.vue";
import { schulformenGymOb } from "~/router/RouteHelper";

export class RouteGostKlausurplanungDetailAnsicht extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
		], "gost.klausurplanung.detailansicht", "detailansicht", SGostKlausurplanungDetailAnsichtVue);
		super.mode = ServerMode.STABLE;
		super.text = "Detailplan";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
	}

	public checkHidden(params?: RouteParams) {
		return checkHiddenKlausurplanungStundenplan(params);
	}

}

export const routeGostKlausurplanungDetailAnsicht = new RouteGostKlausurplanungDetailAnsicht();
