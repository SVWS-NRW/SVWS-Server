import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostKursklausurManager, Schulform, ServerMode, Vector } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungRaumzeitProps } from "~/components/gost/klausurplanung/SGostKlausurplanungRaumzeitProps";

const SGostKlausurplanungRaumzeit = () => import("~/components/gost/klausurplanung/SGostKlausurplanungRaumzeit.vue");

export class RouteGostKlausurplanungRaumzeit extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.raumzeit", "raumzeit", SGostKlausurplanungRaumzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume und Startzeiten";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (!routeGostKlausurplanung.data.hatStundenplanManager)
			return { name: routeGostKlausurplanung.defaultChild!.name, params };
		return false;
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungRaumzeitProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => { return routeGostKlausurplanung.data.hatKursklausurManager ? routeGostKlausurplanung.data.kursklausurmanager : new GostKursklausurManager(routeGostKlausurplanung.data.klausurvorgabenmanager, new Vector(), null, null, null)},
			stundenplanmanager: routeGostKlausurplanung.data.stundenplanmanager,
			createKlausurraum: routeGostKlausurplanung.data.createKlausurraum,
			loescheKlausurraum: routeGostKlausurplanung.data.loescheKlausurraum,
			patchKlausurraum: routeGostKlausurplanung.data.patchKlausurraum,
			erzeugeKlausurraummanager: routeGostKlausurplanung.data.erzeugeKlausurraummanager,
			setzeRaumZuSchuelerklausuren: routeGostKlausurplanung.data.setzeRaumZuSchuelerklausuren,
			patchKlausur: routeGostKlausurplanung.data.patchKlausur,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

}

export const routeGostKlausurplanungRaumzeit = new RouteGostKlausurplanungRaumzeit();

