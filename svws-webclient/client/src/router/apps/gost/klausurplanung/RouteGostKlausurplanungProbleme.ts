import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungProblemeProps } from "~/components/gost/klausurplanung/SGostKlausurplanungProblemeProps";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostKlausurplanungProbleme = () => import("~/components/gost/klausurplanung/SGostKlausurplanungProbleme.vue");

export class RouteGostKlausurplanungProbleme extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.probleme", "probleme", SGostKlausurplanungProbleme);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fehler und Hinweise";
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungProblemeProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			abschnitt: routeGostKlausurplanung.data.abschnitt,
			kMan: () => routeGostKlausurplanung.data.manager,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			erzeugeSchuelerklausuren: routeGostKlausurplanung.data.erzeugeSchuelerklausuren,
			loescheSchuelerklausuren: routeGostKlausurplanung.data.loescheSchuelerklausuren,
			erzeugeKursklausurenAusVorgaben: routeGostKlausurplanung.data.erzeugeKursklausurenAusVorgaben,
			loescheKursklausuren: routeGostKlausurplanung.data.loescheKursklausuren,
			gotoVorgaben: routeGostKlausurplanung.data.gotoVorgaben,
			gotoSchienen: routeGostKlausurplanung.data.gotoSchienen,
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
			gotoNachschreiber: routeGostKlausurplanung.data.gotoNachschreiber,
			gotoStundenplan: routeGostKlausurplanung.data.gotoStundenplan,
			setConfigValue: routeGostKlausurplanung.data.setConfigValue,
			getConfigNumberValue: routeGostKlausurplanung.data.getConfigNumberValue,
		}
	}

}

export const routeGostKlausurplanungProbleme = new RouteGostKlausurplanungProbleme();

