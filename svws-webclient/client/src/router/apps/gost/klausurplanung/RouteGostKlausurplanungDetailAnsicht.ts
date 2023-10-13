import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKursklausurManager, Schulform, ArrayList, ServerMode, GostKlausurvorgabenManager } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungDetailAnsichtProps } from "~/components/gost/klausurplanung/SGostKlausurplanungDetailAnsichtProps";
import SGostKlausurplanungDetailAnsichtVue from "~/components/gost/klausurplanung/SGostKlausurplanungDetailAnsicht.vue";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungDetailAnsicht extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.detailansicht", "detailansicht", SGostKlausurplanungDetailAnsichtVue);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Detailansicht";
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : Number(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungDetailAnsichtProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			faecherManager: routeGostKlausurplanung.data.faecherManager,
			kursklausurmanager: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new GostKlausurvorgabenManager(new ArrayList(), null), new ArrayList(), null)},
			mapLehrer: routeGostKlausurplanung.data.mapLehrer,
			mapSchueler: routeGostKlausurplanung.data.mapSchueler,
			kursmanager: routeGostKlausurplanung.data.kursManager,
			stundenplanmanager: routeGostKlausurplanung.data.stundenplanmanager,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungDetailAnsicht = new RouteGostKlausurplanungDetailAnsicht();

