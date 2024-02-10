import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKursklausurManager, Schulform, ArrayList, ServerMode, GostKlausurvorgabenManager } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungNachschreibAnsichtProps } from "~/components/gost/klausurplanung/SGostKlausurplanungNachschreibAnsichtProps";
import SGostKlausurplanungNachschreibAnsichtVue from "~/components/gost/klausurplanung/SGostKlausurplanungNachschreibAnsicht.vue";

export class RouteGostKlausurplanungNachschreibAnsicht extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.nachschreibansicht", "nachschreibansicht", SGostKlausurplanungNachschreibAnsichtVue);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nachschreibplan";
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : Number(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungNachschreibAnsichtProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(new GostKlausurvorgabenManager(new ArrayList()), new ArrayList(), null, null, null)},
			erzeugeKlausurraummanager: routeGostKlausurplanung.data.erzeugeKlausurraummanager,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungNachschreibAnsicht = new RouteGostKlausurplanungNachschreibAnsicht();

