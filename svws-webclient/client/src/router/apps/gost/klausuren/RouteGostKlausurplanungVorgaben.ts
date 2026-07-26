import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import type { GostKlausurplanungVorgabenProps } from "~/components/gost/klausuren/SGostKlausurplanungVorgabenProps";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostKlausurplanungVorgaben = () => import("~/components/gost/klausuren/SGostKlausurplanungVorgaben.vue");

export class RouteGostKlausurplanungVorgaben extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.vorgaben", "vorgaben", SGostKlausurplanungVorgaben);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vorgaben";
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungVorgabenProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => routeGostKlausurplanung.data.manager,
			erzeugeKlausurvorgabe: routeGostKlausurplanung.data.erzeugeKlausurvorgabe,
			patchKlausurvorgabe: routeGostKlausurplanung.data.patchKlausurvorgabe,
			patchKlausurvorgaben: routeGostKlausurplanung.data.patchKlausurvorgaben,
			loescheKlausurvorgaben: routeGostKlausurplanung.data.loescheKlausurvorgaben,
			erzeugeVorgabenAusVorlage: routeGostKlausurplanung.data.erzeugeVorgabenAusVorlage,
			erzeugeDefaultKlausurvorgaben: routeGostKlausurplanung.data.erzeugeDefaultKlausurvorgaben,
			gotoFach: routeGostKlausurplanung.data.gotoFach,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		};
	}

}

export const routeGostKlausurplanungVorgaben = new RouteGostKlausurplanungVorgaben();
