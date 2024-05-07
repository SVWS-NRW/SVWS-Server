import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, GostKlausurvorgabenManager, GostKursklausurManager, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungVorgabenProps } from "~/components/gost/klausurplanung/SGostKlausurplanungVorgabenProps";
import { routeApp } from "../../RouteApp";

const SGostKlausurplanungVorgaben = () => import("~/components/gost/klausurplanung/SGostKlausurplanungVorgaben.vue");

export class RouteGostKlausurplanungVorgaben extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.vorgaben", "vorgaben", SGostKlausurplanungVorgaben);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vorgaben";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungVorgabenProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			klausurvorgabenmanager: () => { return routeGostKlausurplanung.data.hatKlausurvorgabenManager ? routeGostKlausurplanung.data.klausurvorgabenmanager : new GostKlausurvorgabenManager()},
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager()},
			erzeugeKlausurvorgabe: routeGostKlausurplanung.data.erzeugeKlausurvorgabe,
			patchKlausurvorgabe: routeGostKlausurplanung.data.patchKlausurvorgabe,
			loescheKlausurvorgabe: routeGostKlausurplanung.data.loescheKlausurvorgabe,
			erzeugeVorgabenAusVorlage: routeGostKlausurplanung.data.erzeugeVorgabenAusVorlage,
			erzeugeDefaultKlausurvorgaben: routeGostKlausurplanung.data.erzeugeDefaultKlausurvorgaben,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungVorgaben = new RouteGostKlausurplanungVorgaben();

