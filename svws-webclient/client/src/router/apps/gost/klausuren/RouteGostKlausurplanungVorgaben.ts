import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { type RouteGostKlausurplanung, routeGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";

const SGostKlausurplanungVorgaben = () => import("~/components/gost/klausuren/SGostKlausurplanungVorgaben.vue");

export class RouteGostKlausurplanungVorgaben extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.vorgaben", "vorgaben", SGostKlausurplanungVorgaben);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Vorgaben";
	}

	public getProps() {
		return {
			gotoFach: routeGostKlausurplanung.data.gotoFach,
		};
	}

}

export const routeGostKlausurplanungVorgaben = new RouteGostKlausurplanungVorgaben();
