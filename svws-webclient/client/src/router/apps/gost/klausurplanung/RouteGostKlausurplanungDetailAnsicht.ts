import type { RouteLocationNormalized, RouteParams } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungDetailAnsichtProps } from "~/components/gost/klausurplanung/SGostKlausurplanungDetailAnsichtProps";
import SGostKlausurplanungDetailAnsichtVue from "~/components/gost/klausurplanung/SGostKlausurplanungDetailAnsicht.vue";
import { schulformenGymOb } from "~/router/RouteHelper";

export class RouteGostKlausurplanungDetailAnsicht extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
		], "gost.klausurplanung.detailansicht", "detailansicht", SGostKlausurplanungDetailAnsichtVue);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Detailplan";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (!routeGostKlausurplanung.data.abschnitt || routeGostKlausurplanung.data.manager.stundenplanManagerGeladenAndExistsByAbschnitt(routeGostKlausurplanung.data.abschnitt.id) === false)
			return { name: routeGostKlausurplanung.defaultChild!.name, params };
		return false;
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungDetailAnsichtProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			abschnitt: routeGostKlausurplanung.data.abschnitt,
			kMan: () => routeGostKlausurplanung.data.manager,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungDetailAnsicht = new RouteGostKlausurplanungDetailAnsicht();

