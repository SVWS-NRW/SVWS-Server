import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKlausurvorgabenManager, GostKursklausurManager, Schulform, ArrayList, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungVorgabenProps } from "~/components/gost/klausurplanung/SGostKlausurplanungVorgabenProps";

const SGostKlausurplanungVorgaben = () => import("~/components/gost/klausurplanung/SGostKlausurplanungVorgaben.vue");

export class RouteGostKlausurplanungVorgaben extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.vorgaben", "vorgaben", SGostKlausurplanungVorgaben);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klausurvorgaben";
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : Number(params.abiturjahr);
		return (abiturjahr === undefined);
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungVorgabenProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			klausurvorgabenmanager: () => { return routeGostKlausurplanung.data.hatKlausurvorgabenManager ? routeGostKlausurplanung.data.klausurvorgabenmanager : new GostKlausurvorgabenManager(new ArrayList(), routeGostKlausurplanung.data.faecherManager)},
			kursklausurmanager: () => routeGostKlausurplanung.data.kursklausurmanager,
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

