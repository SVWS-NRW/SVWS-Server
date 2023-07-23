import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKlausurvorgabenManager, GostKursklausurManager, Schulform, ArrayList, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungVorlagenProps } from "~/components/gost/klausurplanung/SGostKlausurplanungVorlagenProps";

const SGostKlausurplanungVorlagen = () => import("~/components/gost/klausurplanung/SGostKlausurplanungVorlagen.vue");

export class RouteGostKlausurplanungVorlagen extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.vorlagen", "vorlagen", SGostKlausurplanungVorlagen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klausurvorlagen";
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined);
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungVorlagenProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new ArrayList(), new ArrayList())},
			klausurvorgabenmanager: () => { return routeGostKlausurplanung.data.hatKlausurvorgabenManager ? routeGostKlausurplanung.data.klausurvorgabenmanager : new GostKlausurvorgabenManager(new ArrayList(), routeGostKlausurplanung.data.faecherManager)},
			mapLehrer: routeGostKlausurplanung.data.mapLehrer,
			erzeugeKlausurvorgabe: routeGostKlausurplanung.data.erzeugeKlausurvorgabe,
			patchKlausurvorgabe: routeGostKlausurplanung.data.patchKlausurvorgabe,
			loescheKlausurvorgabe: routeGostKlausurplanung.data.loescheKlausurvorgabe,
			erzeugeVorgabenAusVorlage: routeGostKlausurplanung.data.erzeugeVorgabenAusVorlage,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungVorlagen = new RouteGostKlausurplanungVorlagen();

