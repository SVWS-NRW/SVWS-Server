import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { type RouteGostKlausurplanung, routeGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";

const SGostKlausurplanungProbleme = () => import("~/components/gost/klausuren/SGostKlausurplanungProbleme.vue");

export class RouteGostKlausurplanungProbleme extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.probleme", "probleme", SGostKlausurplanungProbleme);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Fehler und Hinweise";
	}

	public getProps() {
		return {
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoNachschreiber: routeGostKlausurplanung.data.gotoNachschreiber,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
			gotoSchienen: routeGostKlausurplanung.data.gotoSchienen,
			gotoStundenplan: routeGostKlausurplanung.data.gotoStundenplan,
			gotoVorgaben: routeGostKlausurplanung.data.gotoVorgaben,
		};
	}

}

export const routeGostKlausurplanungProbleme = new RouteGostKlausurplanungProbleme();
