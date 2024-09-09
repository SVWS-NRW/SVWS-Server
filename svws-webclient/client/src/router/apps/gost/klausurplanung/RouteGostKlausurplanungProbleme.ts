import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungProblemeProps } from "~/components/gost/klausurplanung/SGostKlausurplanungProblemeProps";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostKlausurplanungProbleme = () => import("~/components/gost/klausurplanung/SGostKlausurplanungProbleme.vue");

export class RouteGostKlausurplanungProbleme extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		], "gost.klausurplanung.probleme", "probleme", SGostKlausurplanungProbleme);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fehler und Hinweise";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungProblemeProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => routeGostKlausurplanung.data.manager,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			erzeugeSchuelerklausuren: routeGostKlausurplanung.data.erzeugeSchuelerklausuren,
			loescheSchuelerklausuren: routeGostKlausurplanung.data.loescheSchuelerklausuren,
			erzeugeKursklausurenAusVorgaben: routeGostKlausurplanung.data.erzeugeKursklausurenAusVorgaben,
			gotoVorgaben: routeGostKlausurplanung.data.gotoVorgaben,
		}
	}

}

export const routeGostKlausurplanungProbleme = new RouteGostKlausurplanungProbleme();

